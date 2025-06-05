package com.AnnotationPlatform.Core.web;


import com.AnnotationPlatform.Core.bo.*;
import com.AnnotationPlatform.Core.dao.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.AnnotationPlatform.Core.services.IAnnotateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.extras.springsecurity6.util.SpringSecurityContextUtils;

import java.util.*;

@Controller
@RequestMapping("/user")
public class AnnotateurConroller {

    @Autowired
    IAnnotateurService annotateurService;

    @Autowired
    ITacheRepository tacheRepository;

    @Autowired
    ICoupleTextRepository coupleTextRepository;

    @Autowired
    IAnnotationRepository annotationRepository;

    @Autowired
    IClassePossibleRepository classePossibleRepository;

    @Autowired
    IDatasetRepository datasetRepository;

    // Méthode qui calcule l'avancement en pourcentage
    // Méthode qui calcule l'avancement en pourcentage
    public int calculerAvancement(Tache tache, Annotateur annotateur) {
        List<CoupleText> couples = new ArrayList<>(tache.getCoupleTexts());
        long nombreAnnotes = couples.stream()
                .filter(c -> annotationRepository.existsByCoupleTextAndAnnotateur(c, annotateur))
                .count();
        return couples.size() > 0 ? (int) ((nombreAnnotes * 100) / couples.size()) : 0;
    }

    @GetMapping("/")
    public String AnnotateurHomePage(HttpSession session, Model model) {
        Annotateur annotateur = (Annotateur) session.getAttribute("user");
        List<Tache> taches = annotateurService.findAllTaches(annotateur);

        Map<Long, Boolean> tacheStatus = new HashMap<>();
        Map<Long, Integer> avancementMap = new HashMap<>();
        for (Tache tache : taches) {
            boolean complete = tache.getCoupleTexts().stream()
                    .allMatch(c -> annotationRepository.existsByCoupleTextAndAnnotateur(c, annotateur));
            tacheStatus.put(tache.getId(), complete);
            avancementMap.put(tache.getId(), calculerAvancement(tache, annotateur));
        }
        model.addAttribute("nomUser", annotateur.getNom());
        model.addAttribute("prenomUser", annotateur.getPrenom());
        model.addAttribute("taches", taches);
        model.addAttribute("avancementMap", avancementMap);
        model.addAttribute("tacheStatus", tacheStatus);
        model.addAttribute("content", "Annotateur/annotateurHome");
        model.addAttribute("title", "Home Page");
        return "layout";
    }

    @GetMapping("/annotate/{tacheId}")
    public String annotateTache(@PathVariable Long tacheId,
                                @RequestParam(name = "index", defaultValue = "0") int index,
                                HttpSession session,
                                Model model) {
        Annotateur annotateur = (Annotateur) session.getAttribute("user");
        Tache tache = tacheRepository.findById(tacheId).orElse(null);

        if (tache == null || !annotateurService.findAllTaches(annotateur).contains(tache)) {
            return "redirect:/user/";
        }

        Dataset dataset = tache.getDataset();
        List<ClassePossible> classesPossible = classePossibleRepository.getClassePossibleByDataset(dataset);
        List<String> classes = classesPossible.stream()
                .map(ClassePossible::getTextClasse)
                .toList();

        List<CoupleText> coupleList = tache.getCoupleTexts().stream()
                .filter(c -> annotationRepository.getAnnotationByAnnotateurAndCoupleText(annotateur, c) == null)
                .sorted(Comparator.comparing(CoupleText::getId))
                .toList();

        if (coupleList.isEmpty()) {
            return "redirect:/user/tache/terminee";
        }

        if (index < 0 || index >= coupleList.size()) {
            index = 0;
        }

        model.addAttribute("tache", tache);
        model.addAttribute("classes", classes);
        model.addAttribute("coupleList", coupleList);
        model.addAttribute("currentIndex", index);
        model.addAttribute("total", coupleList.size());
        model.addAttribute("content", "Annotateur/annotate");
        model.addAttribute("title", "Annotate Page");
        return "layout";
    }

    @PostMapping("/annotate/{tacheId}/{coupleId}")
    public String handleAnnotation(@PathVariable Long tacheId,
                                   @PathVariable Long coupleId,
                                   @RequestParam String selectedClass,
                                   @RequestParam String action,
                                   @RequestParam int index,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {

        Annotateur annotateur = (Annotateur) session.getAttribute("user");
        CoupleText coupleText = coupleTextRepository.findById(coupleId).orElse(null);
        Tache tache = tacheRepository.findById(tacheId).orElse(null);

        if (coupleText == null || tache == null || !annotateurService.findAllTaches(annotateur).contains(tache)) {
            return "redirect:/user/";
        }

        // Ajouter annotation uniquement si elle n’existe pas
        if ("validate".equals(action)
                && annotationRepository.getAnnotationByAnnotateurAndCoupleText(annotateur, coupleText) == null) {
            annotateurService.annotateCoupleText(annotateur, coupleText, selectedClass);
        }

        List<CoupleText> coupleList = tache.getCoupleTexts().stream()
                .filter(c -> annotationRepository.getAnnotationByAnnotateurAndCoupleText(annotateur, c) == null)
                .sorted(Comparator.comparing(CoupleText::getId))
                .toList();

        if (coupleList.isEmpty()) {
            redirectAttributes.addFlashAttribute("tacheTerminee", true);
            return "redirect:/user/tache/terminee";
        }

        int newIndex = switch (action) {
            case "next","validate" -> index + 1;
            case "previous" -> index - 1;
            default -> index;
        };

        if (newIndex < 0) newIndex = 0;
        if (newIndex >= coupleList.size()) newIndex = coupleList.size() - 1;

        return "redirect:/user/annotate/" + tacheId + "?index=" + newIndex;
    }

    @GetMapping("/dataset/description/{id}")
    public String afficherDescription(@PathVariable Long id, Model model) {
        Optional<Dataset> datasetOpt = datasetRepository.findById(id);
        datasetOpt.ifPresent(dataset -> model.addAttribute("dataset", dataset));
        model.addAttribute("content", "Annotateur/description");
        model.addAttribute("title", "Description du dataset");
        return "layout";
    }

    @GetMapping("/tache/terminee")
    public String afficherFinTache(Model model) {
        model.addAttribute("content", "Annotateur/finTache");
        model.addAttribute("title", "Tâche terminée");
        return "layout";
    }
}
