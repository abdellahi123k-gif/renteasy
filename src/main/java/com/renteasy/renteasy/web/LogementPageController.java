package com.renteasy.renteasy.web;

import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.FileStorageService;
import com.renteasy.renteasy.service.LogementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/logements")
@RequiredArgsConstructor
public class LogementPageController {

    private final LogementService logementService;
    private final SecurityUtils securityUtils;
    private final FileStorageService fileStorageService;

    @GetMapping
    public String list(@RequestParam(required = false) String ville,
                       @RequestParam(required = false) String type,
                       @RequestParam(required = false) BigDecimal minPrix,
                       @RequestParam(required = false) BigDecimal maxPrix,
                       @RequestParam(required = false) Boolean disponible,
                       @PageableDefault(size = 12) Pageable pageable,
                       Model model) {
        try {
            var page = logementService.searchLogements(ville, type, minPrix, maxPrix, disponible, pageable);
            model.addAttribute("page", page);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
        }
        try {
            model.addAttribute("currentUserId", securityUtils.getCurrentUser().getId());
        } catch (Exception e) {
            model.addAttribute("currentUserId", null);
        }
        model.addAttribute("pageTitle", "Logements");
        model.addAttribute("viewContent", "pages/logement/list :: content");
        return "layout/base";
    }

    @GetMapping("/mes-logements")
    public String myLogements(@PageableDefault(size = 12) Pageable pageable, Model model) {
        try {
            var page = logementService.getUserLogements(pageable);
            model.addAttribute("page", page);
        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
        }
        try {
            model.addAttribute("currentUserId", securityUtils.getCurrentUser().getId());
        } catch (Exception e) {
            model.addAttribute("currentUserId", null);
        }
        model.addAttribute("pageTitle", "Mes logements");
        model.addAttribute("viewContent", "pages/logement/mine :: content");
        return "layout/base";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Nouveau logement");
        model.addAttribute("viewContent", "pages/logement/form :: content");
        model.addAttribute("logementForm", new LogementRequestDTO());
        model.addAttribute("logement", LogementResponseDTO.builder().build());
        return "layout/base";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("logementForm") LogementRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Nouveau logement");
            model.addAttribute("viewContent", "pages/logement/form :: content");
            model.addAttribute("logement", LogementResponseDTO.builder().build());
            return "layout/base";
        }
        String imgUrl = fileStorageService.store(dto.getImageFile());
        if (imgUrl != null) dto.setImageUrl(imgUrl);
        String vidUrl = fileStorageService.store(dto.getVideoFile());
        if (vidUrl != null) dto.setVideoUrl(vidUrl);
        logementService.createLogement(dto);
        redirectAttributes.addFlashAttribute("success", "Logement créé avec succès !");
        return "redirect:/logements";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            var logement = logementService.getLogementById(id);
            boolean isOwner = false;
            try {
                isOwner = securityUtils.getCurrentUser().getId().equals(logement.getOwnerId());
            } catch (ResourceNotFoundException ignored) {}
            model.addAttribute("logement", logement);
            model.addAttribute("isOwner", isOwner);
            model.addAttribute("pageTitle", logement.getTitre());
            model.addAttribute("viewContent", "pages/logement/detail :: content");
            return "layout/base";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("error", "Logement introuvable");
            return "redirect:/logements";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            var logement = logementService.getLogementById(id);
            var form = new LogementRequestDTO();
            form.setTitre(logement.getTitre());
            form.setDescription(logement.getDescription());
            form.setVille(logement.getVille());
            form.setAdresse(logement.getAdresse());
            form.setType(logement.getType());
            form.setPrix(logement.getPrix());
            form.setDisponible(logement.isDisponible());
            form.setTelephone(logement.getTelephone());
            form.setImageUrl(logement.getImageUrl());
            form.setVideoUrl(logement.getVideoUrl());
            model.addAttribute("logementForm", form);
            model.addAttribute("logement", logement);
            model.addAttribute("pageTitle", "Modifier: " + logement.getTitre());
            model.addAttribute("viewContent", "pages/logement/form :: content");
            return "layout/base";
        } catch (ResourceNotFoundException e) {
            return "redirect:/logements";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("logementForm") LogementRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("logement", logementService.getLogementById(id));
            model.addAttribute("pageTitle", "Modifier le logement");
            model.addAttribute("viewContent", "pages/logement/form :: content");
            return "layout/base";
        }
        String imgUrl = fileStorageService.store(dto.getImageFile());
        if (imgUrl != null) dto.setImageUrl(imgUrl);
        String vidUrl = fileStorageService.store(dto.getVideoFile());
        if (vidUrl != null) dto.setVideoUrl(vidUrl);
        logementService.updateLogement(id, dto);
        redirectAttributes.addFlashAttribute("success", "Logement mis à jour !");
        return "redirect:/logements";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            logementService.deleteLogement(id);
            redirectAttributes.addFlashAttribute("success", "Logement supprimé !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/logements";
    }
}
