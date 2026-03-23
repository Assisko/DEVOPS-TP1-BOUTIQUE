package fr.boutique.model;

public class LignePanier {

    private final Article article;
    private int quantite;

    public LignePanier(Article article, int quantite) {
        if (article == null)
            throw new IllegalArgumentException("L'article ne peut pas être null");
        if (quantite <= 0)
            throw new IllegalArgumentException("La quantité doit être positive");
        this.article  = article;
        this.quantite = quantite;
    }

    public Article getArticle()    { return article; }
    public int getQuantite()       { return quantite; }
    public void setQuantite(int q) { this.quantite = q; }
    public double getSousTotal()   { return article.getPrix() * quantite; }
}
