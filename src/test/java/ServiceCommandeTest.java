import fr.boutique.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceCommandeTest {

    private DepotStock stockDisponible;
    private ServiceCommande service;
    private Panier panier;
    private Article articleTest;

    @BeforeEach
    void initialiser() {
        stockDisponible = reference -> 100;
        service = new ServiceCommande(stockDisponible);
        panier = new Panier();
        articleTest = new Article("REF-001", "Cahier", 3.50);
    }

    @Test
    void commandeValideDoitRetournerUneCommande() {
        // Arranger
        panier.ajouterArticle(articleTest, 2);

        // Agir
        Commande commande = service.passerCommande(panier, "CLIENT-42");

        // Affirmer
        assertNotNull(commande);
        assertEquals(7.0, commande.total(), 0.001);
        assertEquals("CLIENT-42", commande.identifiantClient());
        assertNotNull(commande.dateCreation());
    }

    @Test
    void panierVideDoitLeverIllegalStateException() {
        // Affirmer
        assertThrows(IllegalStateException.class, () ->
                service.passerCommande(panier, "C1"));
    }

    @Test
    void identifiantClientNulDoitLeverException() {
        // Arranger
        panier.ajouterArticle(articleTest, 1);

        // Affirmer
        assertThrows(IllegalArgumentException.class, () ->
                service.passerCommande(panier, null));
    }

    @Test
    void identifiantClientVideDoitLeverException() {
        // Arranger
        panier.ajouterArticle(articleTest, 1);

        // Affirmer
        assertThrows(IllegalArgumentException.class, () ->
                service.passerCommande(panier, ""));
    }

    @Test
    void stockInsuffisantDoitLeverStockInsuffisantException() {
        // Arranger
        DepotStock stockInsuffisant = reference -> 1;
        ServiceCommande serviceAvecStockFaible = new ServiceCommande(stockInsuffisant);
        panier.ajouterArticle(articleTest, 5);

        // Affirmer
        assertThrows(StockInsuffisantException.class, () ->
                serviceAvecStockFaible.passerCommande(panier, "CLIENT-99"));
    }

    @Test
    void totalCommandeDoitCorrespondreAuTotalDuPanier() {
        // Arranger
        panier.ajouterArticle(articleTest, 2); // 7.0
        Article article2 = new Article("REF-002", "Stylo", 1.50);
        panier.ajouterArticle(article2, 2); // 3.0
        double totalPanier = panier.calculerTotal(); // 10.0

        // Agir
        Commande commande = service.passerCommande(panier, "CLIENT-10");

        // Affirmer
        assertEquals(totalPanier, commande.total(), 0.001);
    }
}