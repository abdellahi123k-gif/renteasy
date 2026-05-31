package com.renteasy.renteasy.web;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.service.LogementService;
import com.renteasy.renteasy.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@Controller
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationPageController {

    private final ReservationService reservationService;
    private final LogementService logementService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("reservations", reservationService.getAllReservations());
        model.addAttribute("pageTitle", "Mes réservations");
        model.addAttribute("viewContent", "pages/reservation/list :: content");
        return "layout/base";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(required = false) Long logementId, Model model) {
        model.addAttribute("pageTitle", "Nouvelle réservation");
        model.addAttribute("viewContent", "pages/reservation/form :: content");
        model.addAttribute("reservationForm", new ReservationRequestDTO());
        if (logementId != null) {
            try {
                model.addAttribute("logement", logementService.getLogementById(logementId));
            } catch (Exception e) {
                // logement not found, ignore
            }
        }
        return "layout/base";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("reservationForm") ReservationRequestDTO dto,
                         BindingResult result,
                         @RequestParam Long logementId,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        dto.setLogementId(logementId);
        if (result.hasErrors()) {
            model.addAttribute("pageTitle", "Nouvelle réservation");
            model.addAttribute("viewContent", "pages/reservation/form :: content");
            return "layout/base";
        }
        try {
            reservationService.createReservation(dto);
            redirectAttributes.addFlashAttribute("success", "Réservation effectuée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reservations/new?logementId=" + logementId;
        }
        return "redirect:/reservations";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        try {
            var reservation = reservationService.getReservationById(id);
            model.addAttribute("reservation", reservation);
            model.addAttribute("pageTitle", "Réservation #" + id);
            model.addAttribute("viewContent", "pages/reservation/detail :: content");
            return "layout/base";
        } catch (Exception e) {
            return "redirect:/reservations";
        }
    }

    @PostMapping("/{id}/confirm")
    public String confirm(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.confirmReservation(id);
            redirectAttributes.addFlashAttribute("success", "Réservation confirmée !");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations/" + id;
    }

    @PostMapping("/{id}/cancel")
    public String cancel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reservationService.cancelReservation(id);
            redirectAttributes.addFlashAttribute("success", "Réservation annulée.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/reservations/" + id;
    }
}
