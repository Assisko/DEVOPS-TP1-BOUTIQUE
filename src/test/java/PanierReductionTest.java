import fr.boutique.Article;
import fr.boutique.Panier;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PanierReductionTest {

    @ParameterizedTest
    @CsvSource({
            "'', 100.0",
            "REDUC10, 90.0",
            "REDUC20, 80.0"
    })
    void calculerTotalDoitAppliquerLaBonneReduction(String code, double totalAttendu) {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Classeur", 10.0);
        panier.ajouterArticle(article, 10); // 100.0

        // Agir
        if (code != null && !code.isBlank()) {
            panier.appliquerCodeReduction(code.trim());
        }

        // Affirmer
        assertEquals(totalAttendu, panier.calculerTotal(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "'', 50.0",
            "REDUC10, 45.0",
            "REDUC20, 40.0"
    })
    void calculerTotalAvecPlusieursQuantitesDoitAppliquerLaBonneReduction(String code, double totalAttendu) {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-002", "Carnet", 25.0);
        panier.ajouterArticle(article, 2); // 50.0

        // Agir
        if (code != null && !code.isBlank()) {
            panier.appliquerCodeReduction(code.trim());
        }

        // Affirmer
        assertEquals(totalAttendu, panier.calculerTotal(), 0.001);
    }

    @ParameterizedTest
    @CsvSource({
            "'', 1, 10.0",
            "REF-001, 0, 10.0",
            "REF-001, -2, 10.0",
            "REF-001, 1, -5.0"
    })
    void ajouterArticleParametreInvalideDoitLeverException(String reference, int quantite, double prix) {
        // Arranger
        Panier panier = new Panier();

        // Affirmer
        assertThrows(IllegalArgumentException.class, () -> {
            Article article = new Article(reference, "Produit test", prix);
            panier.ajouterArticle(article, quantite);
        });
    }
}