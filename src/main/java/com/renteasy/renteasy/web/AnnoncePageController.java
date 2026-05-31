package com.renteasy.renteasy.web;

import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.AnnonceService;
import com.renteasy.renteasy.service.LogementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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

import java.util.List;

@Controller
@RequestMapping("/annonces")
@RequiredArgsConstructor
public class AnnoncePageController {

    private final AnnonceService annonceService;
    private final LogementService logementService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public String list(@RequestParam(required = false) String active, Model model) {
        List<?> annonces;
        if ("true".equals(active)) {
            annonces = annonceService.getActiveAnnonces();
        } else {
            annonces = annonceService.getAllAnnonces();
        }
        model.addAttribute("annonces", annonces);
        try {
            model.addAttribute("currentUserId", securityUtils.getCurrentUser().getId());
        } catch (Exception e) {
            model.addAttribute("currentUserId", null);
        }
        model.addAttribute("pageTitle", "Annonces");
        model.addAttribute("viewContent", "pages/annonce/list :: content");
        return "layout/base";
    }

    @GetMapping("/mes-annonces")
    public String myAnnonces(Model model) {
        try {
            model.addAttribute("annonces", annonceService.getUserAnnonces());
        } catch (Exception e) {
            model.addAttribute("error", "Erreur: " + e.getMessage());
        }
        try {
            model.addAttribute("currentUserId", securityUtils.getCurrentUser().getId());
        } catch (Exception e) {
            model.addAttribute("currentUserId", null);
        }
        model.addAttribute("pageTitle", "Mes annonces");
        model.addAttribute("viewContent", "pages/annonce/mine :: content");
        return "layout/base";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("pageTitle", "Nouvelle annonce");
        model.addAttribute("viewContent", "pages/annonce/form :: content");
        model.addAttribute("annonceForm", new AnnonceRequestDTO());
        model.addAttribute("annonce", null);
        return "layout/base";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        try {
            var annonce = annonceService.getAnnonceById(id);
            var form = new AnnonceRequestDTO();
            form.setTitre(annonce.getTitre());
            form.setDescription(annonce.getDescription());
            form.setLogementId(annonce.getLogementId());
            model.addAttribute("annonceForm", form);
            model.addAttribute("annonce", annonce);
            model.addAttribute("pageTitle", "Modifier: " + annonce.getTitre());
            model.addAttribute("viewContent", "pages/annonce/form :: content");
            return "layout/base";
        } catch (ResourceNotFoundException e) {
            return "redirect:/annonces";
        }
    }

    @ModelAttribute("userLogements")
    public List<LogementResponseDTO> getUserLogements() {
        return logementService.getAllLogements(Pageable.ofSize(100)).getContent();
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("annonceForm") AnnonceRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Nouvelle annonce");
            model.addAttribute("viewContent", "pages/annonce/form :: content");
            model.addAttribute("annonce", null);
            return "layout/base";
        }
        annonceService.createAnnonce(dto);
        redirectAttributes.addFlashAttribute("success", "Annonce publiée !");
        return "redirect:/annonces";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("annonceForm") AnnonceRequestDTO dto,
                         BindingResult result,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        if (result.hasErrors()) {
            model.addAttribute("annonce", annonceService.getAnnonceById(id));
            model.addAttribute("pageTitle", "Modifier l'annonce");
            model.addAttribute("viewContent", "pages/annonce/form :: content");
            return "layout/base";
        }
        annonceService.updateAnnonce(id, dto);
        redirectAttributes.addFlashAttribute("success", "Annonce mise à jour !");
        return "redirect:/annonces";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            annonceService.deleteAnnonce(id);
            redirectAttributes.addFlashAttribute("success", "Annonce supprimée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur: " + e.getMessage());
        }
        return "redirect:/annonces";
    }
}
