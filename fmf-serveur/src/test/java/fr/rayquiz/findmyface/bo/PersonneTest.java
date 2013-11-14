package fr.rayquiz.findmyface.bo;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import fr.rayquiz.findmyface.utils.Phonetic;

public class PersonneTest {
    @Test
    public void should_generate_phonetic() {
        // Arrange
        Personne p = new Personne();
        String nom = "Mister Complexe Phonex";
        String prenom = "Prenom de phonetique";

        // Act
        p.setNom(nom);
        p.setPrenom(prenom);

        // Assert
        assertThat(p.getNomPhonetic()).isNotNull().isEqualTo(Phonetic.genererPhonetic(nom));
        assertThat(p.getPrenomPhonetic()).isNotNull().isEqualTo(Phonetic.genererPhonetic(prenom));
    }

}
