package com.AnnotationPlatform.Core.services.Impl;

import com.AnnotationPlatform.Core.bo.*;
import com.AnnotationPlatform.Core.dao.*;
import com.AnnotationPlatform.Core.dto.AnnotateurDTO;
import com.AnnotationPlatform.Core.dto.DatasetDTO;
import com.AnnotationPlatform.Core.dto.annDTO;
import com.AnnotationPlatform.Core.services.IAdminService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;


@Service
@Transactional
public class AdminServiceImpl implements IAdminService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private  IAdminRepository adminRepo;
    @Autowired
    private IAnnotateurRepository annotateurRepo;
    @Autowired
    private IDatasetRepository datasetRepo;
    @Autowired
    private ITacheRepository tacheRepo;

    @Autowired
    private IAnnotationRepository annotationRepo;

    @Override
    public Dataset createDataset(Dataset dataset) {
        return datasetRepo.save(dataset);
    }

    @Override
    public List<Dataset> findAllDataset() {
        return datasetRepo.findAll();
    }

    @Override
    public Optional<Dataset> readDataset(Long id) {
        return datasetRepo.findById(id);
    }


    @Override
    public List<Annotateur> findAllAnnotateurs() {

        return annotateurRepo.findAll();
    }

    @Override
    public Optional<Annotateur> readAnnotateur(Long id) {
        return annotateurRepo.findById(id);
    }

    @Override
    public Annotateur updateAnnotateur(Annotateur annotateur) {
        return annotateurRepo.save(annotateur);
    }

    @Override
    public void deleteAnnotateur(Long id) {
        Annotateur annotateur = annotateurRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Annotateur introuvable"));
        annotateur.setActive(false); // Désactive l'annotateur au lieu de le supprimer
        annotateurRepo.save(annotateur);
    }

    @Override
    public void affecterAnnotateurADataset(Long annotateurId, Long TacheId) {
        Optional<Annotateur> ann = annotateurRepo.findById(annotateurId);
        Optional<Tache> tache = tacheRepo.findById(TacheId);
        if (ann.isPresent() && tache.isPresent()) {
            Annotateur annotateur = ann.get();
            Tache t=tache.get();
            List<Tache> taches=annotateur.getTaches();
            taches.add(t);
            annotateur.setTaches(taches);
            annotateurRepo.save(annotateur);

        }else {
            throw new IllegalArgumentException("Annotateur ou Dataset non trouvé");
        }

    }

    @Override
    public long countAllDatasets() {
        return adminRepo.countAllDatasets();
    }


    @Override
    public long countAllAnnotators() {
        return adminRepo.countAllAnnotators();
    }
    public String generateSecurePassword() {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String symbols = "!@#$%^&*()-_+=";

        String allChars = upper + lower + digits + symbols;
        SecureRandom random = new SecureRandom();

        StringBuilder password = new StringBuilder();

        // Garantir au moins un caractère de chaque type
        password.append(upper.charAt(random.nextInt(upper.length())));
        password.append(lower.charAt(random.nextInt(lower.length())));
        password.append(digits.charAt(random.nextInt(digits.length())));
        password.append(symbols.charAt(random.nextInt(symbols.length())));

        // Ajouter d'autres caractères aléatoires
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        // Mélanger les caractères (optionnel mais conseillé)
        List<Character> pwdChars = new ArrayList<>(
                password.chars()
                        .mapToObj(c -> (char) c)
                        .toList()
        );
        Collections.shuffle(pwdChars, random); // ✅ fonctionne maintenant

        // Recomposer la chaîne finale
        StringBuilder finalPassword = new StringBuilder();
        for (Character c : pwdChars) {
            finalPassword.append(c);
        }

        return finalPassword.toString();
    }

    public annDTO createAnnotateur(Annotateur annotateur) {
        String plainPassword = generateSecurePassword();
        String hashedPassword = passwordEncoder.encode(plainPassword);
        annotateur.setPassword(hashedPassword);
        annotateurRepo.save(annotateur);
        return new annDTO(annotateur,plainPassword);
    }

    public List<Annotation> findAnnotationsByDataset(Long datasetId) {
        return annotationRepo.findByDatasetId(datasetId);
    }


    public String exportAnnotationsToCSV(Long datasetId) {
        String filePath = "models/dataset_" + datasetId + ".csv"; // Chemin du fichier CSV

        try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8);
             PrintWriter writer = new PrintWriter(fileWriter)) {

            writer.println("text1,text2,classe"); // En-tête du fichier CSV

            List<Annotation> annotations = annotationRepo.findByDatasetId(datasetId);

            for (Annotation annotation : annotations) {
                writer.println(annotation.getCoupleText().getText1() + "," +
                        annotation.getCoupleText().getText2() + "," +
                        annotation.getClasse());
            }
            System.out.println(filePath);

            return filePath; // Retourne le chemin du fichier CSV généré

        } catch (IOException e) {
            e.printStackTrace();
            return null; // En cas d'erreur, retourne null ou un message d'erreur
        }
    }




}
