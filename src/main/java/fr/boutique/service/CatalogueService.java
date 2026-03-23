package fr.boutique.service;

import fr.boutique.model.Article;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogueService {

    private static final List<Article> CATALOGUE = List.of(
        new Article("REF-001", "Stylo bille bleu",      1.50,  "Papeterie"),
        new Article("REF-002", "Cahier 100 pages",      3.90,  "Papeterie"),
        new Article("REF-003", "Règle 30 cm",           1.20,  "Papeterie"),
        new Article("REF-004", "Classeur A4",           4.50,  "Papeterie"),
        new Article("REF-005", "Sac à dos 20L",        24.99,  "Maroquinerie"),
        new Article("REF-006", "Trousse zippée",        7.90,  "Maroquinerie"),
        new Article("REF-007", "Agenda 2024",           9.99,  "Papeterie"),
        new Article("REF-008", "Calculatrice simple",  12.50,  "Electronique")
    );

    public List<Article> findAll() {
        return CATALOGUE;
    }

    public Article findByReference(String reference) {
        return CATALOGUE.stream()
                .filter(a -> a.getReference().equals(reference))
                .findFirst()
                .orElse(null);
    }
}
