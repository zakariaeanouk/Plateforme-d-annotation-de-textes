package com.AnnotationPlatform.Core.web;

import com.AnnotationPlatform.Core.bo.*;
import com.AnnotationPlatform.Core.dao.*;
import com.AnnotationPlatform.Core.dto.*;
import com.AnnotationPlatform.Core.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.*;


import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IDatasetService datasetService;

    @Autowired
    private IAnnotateurService annotateurService;

    @Autowired
    private IClassePossibleService classePossibleService;

    @Autowired
    private ICoupleText coupleTextService;

    @Autowired
    private ITacheService tacheService;

    @Autowired
    private IRoleRepository rolerepository;

    @Autowired
    private ICoupleTextRepository coupleTextRepository;

    @Autowired
    private IAnnotationRepository annotationRepository;

    @Autowired
    private IDatasetRepository datasetRepository;

    @Autowired
    private ICoupleTextRepository coreCoupleTextRepository;


    // Admin Home Page
    @GetMapping("/")
    public String AdminHomePage(Model model){

        model.addAttribute("datasetsNumber",adminService.countAllDatasets());
        model.addAttribute("annotatorsNumber",adminService.countAllAnnotators());
        model.addAttribute("textPairsNumber", coupleTextRepository.count());
        model.addAttribute("completedAnnotations",annotationRepository.count());
        model.addAttribute("content", "Admin/adminHome");
        model.addAttribute("title", "Home");

        return "layout";
    }


    // Show all datasets in database
    @GetMapping("/datasets")
    public String datasetsPage(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){

      Pageable pageable = PageRequest.of(page, size);
      Page<DatasetDTO> datasetPage = datasetService.getAllDatasets(pageable);


        Map<Long, Integer> progressMap = new HashMap<>();
        List<DatasetDTO> datasets = datasetPage.getContent();

        for (DatasetDTO dataset : datasets) {
            long total = coupleTextRepository.countByDataset_Id(dataset.getId());
            long annotated = annotationRepository.countByCoupleText_Dataset_Id(dataset.getId());

            int percent = total > 0 ? (int) ((annotated * 100.0) / total) : 0;
            progressMap.put(dataset.getId(), percent);
        }

        model.addAttribute("progressMap", progressMap);

        model.addAttribute("datasets", datasetPage.getContent());
      model.addAttribute("currentPage", page);
      model.addAttribute("totalPages", datasetPage.getTotalPages());

      model.addAttribute("content", "Admin/datasetPage");
      model.addAttribute("title", "Datasets");

        return "layout";
    }


    // Form to add dataset
    @GetMapping("/addDataset")
    public String ShowDatasetForm(Model model){

        model.addAttribute("content", "Admin/datasetForm");
        model.addAttribute("title", "Add Dataset");

        return "layout";
    }



    // To delete dataset
    @GetMapping("/dataset/delete/{id}")
    public String deleteDataset(@PathVariable("id") Long id){

        datasetService.deleteDataset(id);

        return "redirect:/admin/datasets";
    }


    // Add dataset to database
    @PostMapping("/dataset")
    public String addDataset(@RequestParam("datasetName") String datasetName,
                             @RequestParam("datasetDescription") String datasetDescription,
                             @RequestParam("datasetPossibleClasses") String datasetPossibleClasses,
                             @RequestParam("datasetFile") MultipartFile datasetFile,
                             @RequestParam("dateLimit") String dateLimit) throws IOException {


        // create dataset object and save it
        Dataset dataset = new Dataset();
        dataset.setDescriptionDataset(datasetDescription);
        dataset.setNomDataset(datasetName);
        datasetService.saveDataset(dataset);
        long datasetId = dataset.getId();

        // save all classe possible
        List<String> classePossible = List.of(datasetPossibleClasses.split(";"));
        classePossibleService.saveListOfClassePossible(classePossible,datasetId);

        // parse file and save couple texts along with dataset id
        coupleTextService.parseFileAndSaveCoupleText(datasetFile, datasetId);

        tacheService.divideDatasetIntoTasks(datasetId, dateLimit);

        return "redirect:/admin/datasets";
    }




    // to show all annotators for assignement
    @GetMapping("/dataset/{id}/assign")
    public String showAllAnnotateurs(@PathVariable("id") long idDataset, Model model,  @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size){

        Pageable pageable = PageRequest.of(page, size);
        Page<AnnotateurDTO> annotateursPage = annotateurService.findAllAnnotator(pageable);

        model.addAttribute("annotateurs", annotateursPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", annotateursPage.getTotalPages());


        model.addAttribute("AnnotateursForm", new AnnotateurSelectionFormDTO());
        model.addAttribute("id", idDataset);

        model.addAttribute("content", "Admin/annotateursAffectationPage");
        model.addAttribute("title", "Affectation Annotateurs");


        return "layout";
    }



    // to assign selected annotators to a dataset
    @PostMapping("/dataset/{id}/assign")
    public String assignAnnotators(@PathVariable("id") Long idDataset, @ModelAttribute AnnotateurSelectionFormDTO form) {

        List<Long> selectedIds = form.getSelectedAnnotatorIds();

        List<Tache> tachesList = tacheService.getTachesByDatasetId(idDataset);
        tacheService.assignAnnotatorsToDataset(tachesList, selectedIds);

        return "redirect:/admin/datasets";
    }


    // show dataset details with content
    @GetMapping("/dataset/{id}/details")
    public String showDatasetDetails(@PathVariable("id") long idDataset,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "5") int size,
                                     Model model){


        Dataset dataset = datasetService.findById(idDataset);

        Pageable pageable = PageRequest.of(page, size);
        Page<CoupleText> coupleTextsPage = coupleTextService.getCoupleTextsByDatasetId(idDataset, pageable);


        DatasetDTO datasetDTO = new DatasetDTO();
            datasetDTO.setId(dataset.getId());
            datasetDTO.setNomDataset(dataset.getNomDataset());
            datasetDTO.setDescriptionDataset(dataset.getDescriptionDataset());
            datasetDTO.setClasseCount(dataset.getClassePossibles().size());
            datasetDTO.setCoupleCount((int) coupleTextsPage.getTotalElements());

        model.addAttribute("title", "Dataset Details");
        model.addAttribute("content", "Admin/datasetDetailsPage");

        model.addAttribute("dataset", datasetDTO);
        model.addAttribute("coupleTextsPage", coupleTextsPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coupleTextsPage.getTotalPages());


        return "layout";
    }


    // show all annotators
    @GetMapping("/annotators")
    public String findAllAnnotateurs(Model model,@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<AnnotateurDTO> annotateursPage = annotateurService.findAllAnnotator(pageable);

        model.addAttribute("annotateurs", annotateursPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", annotateursPage.getTotalPages());

        model.addAttribute("content", "Admin/annotateurs");
        model.addAttribute("title", "Annotateurs");

        return "layout";
    }


    // show annotators addition form
    @GetMapping("/annotators/add")
    public String ShowAnnotatorForm(Model model){

        model.addAttribute("content","admin/annotateurForm");
        model.addAttribute("title", "Add Annotateur");

        return "layout";
    }

    // add annotator to database
    @PostMapping("/annotators/add")
    public String addAnnotator(@ModelAttribute Annotateur annotateur, RedirectAttributes redirectAttributes) {
        Role role = rolerepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("Rôle introuvable"));
        annotateur.setRole(role);
        annotateur.setLogin((annotateur.getNom() + "." + annotateur.getPrenom()).replaceAll("\\s+", "").toLowerCase() + "@gmail.com");

        annDTO ann=adminService.createAnnotateur(annotateur);
        redirectAttributes.addFlashAttribute("password", ann.getPlainPassword());
        redirectAttributes.addFlashAttribute("email", ann.getAnnotateur().getLogin());
        return "redirect:/admin/annotators";
    }


    // delete annotator
    @PostMapping("/annotators/delete/{id}")
    public String deleteAnnotator(@PathVariable("id") Long id){

        adminService.deleteAnnotateur(id);

        return "redirect:/admin/annotators";
    }
    @GetMapping("/annotations/export/{datasetId}")
    public void exportAnnotationsToCSV(@PathVariable Long datasetId, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=annotations_" + datasetId + ".csv");

        PrintWriter writer = response.getWriter();
        writer.println("text1,text2,classe"); // En-tête du fichier CSV

        // Récupérer les annotations associées au dataset
        List<Annotation> annotations = adminService.findAnnotationsByDataset(datasetId);

        if (annotations.isEmpty()) {
            writer.println("Aucune annotation trouvée pour ce dataset.");
            return;
        }

        for (Annotation annotation : annotations) {
            writer.println(annotation.getCoupleText().getText1() + "," +
                    annotation.getCoupleText().getText2() + "," +
                    annotation.getClasse());
        }

        writer.flush();
        writer.close();
    }

    // méthodes ajoutéés
    @GetMapping("/model")
    public String showModelTrainingPage(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DatasetDTO> datasetPage = datasetService.getCompletedDatasets(pageable);

        model.addAttribute("datasets", datasetPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", datasetPage.getTotalPages());

        model.addAttribute("content", "Admin/modelTrainingPage");
        model.addAttribute("title", "Entraînement du modèle");

        return "layout";
    }


    // CODE  CODE ADDED
    @PostMapping("/model/train/{datasetId}")
    public ResponseEntity<String> launchTraining(@PathVariable Long datasetId) {
        try {
            // 1. Exporter les annotations
            String csvFilePath = adminService.exportAnnotationsToCSV(datasetId);
            String pythonScript = "models/train.py";

            // 2. Lancer le script Python
            ProcessBuilder pb = new ProcessBuilder("python3", pythonScript, csvFilePath);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            System.out.println("Script Python lancé : " + pythonScript + " avec " + csvFilePath);

            // 3. Lire la sortie
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[Python Output] " + line); // Ajoute cette ligne pour voir la sortie en console
                output.append(line).append("\n");
            }
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erreur : le script s'est terminé avec le code " + exitCode + "\n" + output);
            }
            System.out.println("train finished");

            return ResponseEntity.ok("Entraînement terminé pour le dataset " + datasetId + ".\n\n" + output);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du lancement de l'entraînement : " + e.getMessage());
        }
    }







}
