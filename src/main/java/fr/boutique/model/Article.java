package fr.boutique.model;

public class Article {

    private final String reference;
    private final String nom;
    private final double prix;
    private final String categorie;

    public Article(String reference, String nom, double prix, String categorie) {
        if (reference == null || reference.isBlank())
            throw new IllegalArgumentException("La référence ne peut pas être vide");
        if (nom == null || nom.isBlank())
            throw new IllegalArgumentException("Le nom ne peut pas être vide");
        if (prix < 0)
            throw new IllegalArgumentException("Le prix ne peut pas être négatif");

        this.reference = reference;
        this.nom       = nom;
        this.prix      = prix;
        this.categorie = categorie;
    }

    public String getReference() { return reference; }
    public String getNom()       { return nom; }
    public double getPrix()      { return prix; }
    public String getCategorie() { return categorie; }
}
