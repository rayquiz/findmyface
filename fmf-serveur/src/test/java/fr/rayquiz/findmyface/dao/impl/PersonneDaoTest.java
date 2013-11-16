package fr.rayquiz.findmyface.dao.impl;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.googlecode.objectify.NotFoundException;
import com.googlecode.objectify.ObjectifyService;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.bo.Indice;
import fr.rayquiz.findmyface.bo.Personne;
import fr.rayquiz.findmyface.dao.IIdGeneratorService;
import fr.rayquiz.findmyface.tests.GaeDefaultTestClass;

public class PersonneDaoTest extends GaeDefaultTestClass {

    private PersonneDao personneDao;
    private IIdGeneratorService idGenerator;

    @Override
    public void setUp() {
        super.setUp();
        personneDao = new PersonneDao();
        idGenerator = mock(IIdGeneratorService.class);
        personneDao.setIdGeneratorService(idGenerator);

        doAnswer(new MockIdGenerator()).when(idGenerator).generateNewEntityId(Matchers.any(Difficulte.class));
    }

    @Test
    public void should_save_entity_immediately() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Marcel");
        p.setPrenom("Rene");
        p.setDifficulte(Difficulte.SIMPLE);
        p.setAnneeDeces(2006);
        p.setAnneeNaissance(1950);

        // Act
        long idRetour = personneDao.saveImmediate(p);

        // Assert
        assertThat(p.getId()).isNotNull().isNotEqualTo(0).isEqualTo(idRetour);
        assertThat(ObjectifyService.ofy().load().type(Personne.class).count()).isEqualTo(1);
    }

    @Test
    public void should_save_entity_with_indices_immediately() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Nom");
        p.setPrenom("Prenom");
        p.setDifficulte(Difficulte.SIMPLE);
        p.getIndiceListe().add(new Indice(1, "enonce 1"));

        // Act
        long idRetour = personneDao.saveImmediate(p);
        long nbEnregistrement = ObjectifyService.ofy().load().type(Personne.class).count();

        // Assert
        assertThat(p.getId()).isNotNull().isNotEqualTo(0).isEqualTo(idRetour);
        assertThat(nbEnregistrement).isEqualTo(1);
    }

    @Test
    public void should_save_entity_asynchronously() {
        // Arrange
        Personne p = new Personne();
        p.setNom("Nom");
        p.setPrenom("Prenom");
        p.setDifficulte(Difficulte.SIMPLE);

        // Act
        personneDao.saveAsynchronously(p);

        // Assert
    }

    @Test
    public void should_load_entity() {
        // Arrange
        Personne retour = null;
        Personne p = new Personne();
        p.setNom("Mon prénom");
        p.setPrenom("Mon nom");
        p.setDifficulte(Difficulte.SIMPLE);

        // Act
        long idRetour = personneDao.saveImmediate(p);
        retour = personneDao.getById(idRetour);

        // Assert
        assertThat(retour).isEqualTo(p);
    }

    @Test
    public void should_load_entity_with_indices() {
        // Arrange
        Personne retour = null;
        Personne p = new Personne();
        p.setNom("Mon prénom");
        p.setPrenom("Mon nom");
        p.setDifficulte(Difficulte.SIMPLE);
        p.getIndiceListe().add(new Indice(1, "enonce 1"));
        p.getIndiceListe().add(new Indice(2, "enonce 2"));

        // Act
        long idRetour = personneDao.saveImmediate(p);
        retour = personneDao.getById(idRetour);

        // Assert
        assertThat(retour).isEqualTo(p);
        assertThat(p.getIndiceListe()).isSameAs(retour.getIndiceListe());
    }

    @Test
    public void should_find_personne_by_nom() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");

        // Act
        saveAll(liste);
        Set<Personne> retour = personneDao.getByNomOuPrenomPhonetic("Einst");

        // Assert
        assertThat(retour).containsOnly(liste.get(5), liste.get(8));
    }

    @Test
    public void should_find_personne_by_prenom() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");
        liste.get(1).withNom("Michel").withPrenom("Jean-Pierre");

        // Act
        saveAll(liste);
        Set<Personne> retour = personneDao.getByNomOuPrenomPhonetic("jean");

        // Assert
        assertThat(retour).containsOnly(liste.get(8), liste.get(1));
    }

    @Test
    public void should_find_personne_whith_same_prenom_and_nom_without_double() {
        // Arrange
        List<Personne> liste = generatePersonnes(10);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("EinsteinBerger").withPrenom("Jean-Rene");
        liste.get(2).withNom("Michel").withPrenom("Michel");

        // Act
        saveAll(liste);
        Set<Personne> retour = personneDao.getByNomOuPrenomPhonetic("michel");

        // Assert
        assertThat(retour).hasSize(1).containsOnly(liste.get(2));
    }

    @Test
    public void should_find_personne_by_prenom_and_nom() {
        // Arrange
        List<Personne> liste = generatePersonnes(20);
        liste.get(5).withNom("Einstein").withPrenom("Albert");
        liste.get(8).withNom("Miste").withPrenom("Pierre-Mathieu");
        liste.get(2).withNom("Chedid").withPrenom("Mathieu");
        liste.get(7).withNom("Mathieu").withPrenom("Mireille");

        // Act
        saveAll(liste);
        Set<Personne> retour = personneDao.getByNomOuPrenomPhonetic("matthieu");

        // Assert
        assertThat(retour).hasSize(2).containsOnly(liste.get(2), liste.get(7));
    }

    @Test
    public void should_get_random_personne() {
        // Arrange
        int maxEl = 20;
        MockIdBuilder idBuilder = new MockIdBuilder();
        List<Personne> liste = generatePersonnes(maxEl);

        when(idGenerator.getCount(Difficulte.SIMPLE)).thenReturn((long) maxEl);
        doAnswer(idBuilder).when(idGenerator)
                .buildEntityIdFromInfos(Matchers.any(Difficulte.class), Matchers.anyLong());

        // Act
        saveAll(liste);
        for (int i = 0; i < 10; i++) {
            Personne p = personneDao.getRandomByDifficulte(Difficulte.SIMPLE);

            // Assert
            assertThat(p).isSameAs(liste.get((int) idBuilder.getLastId() - 1));
        }
    }

    @Test(expected = NotFoundException.class)
    public void should_throw_exception_on_0_count() {
        // Arrange
        when(idGenerator.getCount(Difficulte.SIMPLE)).thenReturn((long) 0);

        // Act
        personneDao.getRandomByDifficulte(Difficulte.SIMPLE);

        // Assert
        Assert.fail("Exception attendue");
    }

    @Test(expected = NotFoundException.class)
    public void should_throw_exception_on_no_personne_available() {
        // Arrange
        when(idGenerator.getCount(Difficulte.SIMPLE)).thenReturn((long) 10);
        doAnswer(new MockIdBuilder()).when(idGenerator).buildEntityIdFromInfos(Matchers.eq(Difficulte.SIMPLE),
                Matchers.anyLong());

        // Act
        personneDao.getRandomByDifficulte(Difficulte.SIMPLE);

        // Assert
        Assert.fail("Exception attendue");
    }

    @Test
    public void should_get_random_personne_whitout_difficulte() {
        // Arrange
        int maxEl = 20;
        MockIdBuilder idBuilder = new MockIdBuilder();
        List<Personne> liste = generatePersonnes(maxEl);

        when(idGenerator.getCount(Matchers.any(Difficulte.class))).thenReturn((long) maxEl);
        doAnswer(idBuilder).when(idGenerator)
                .buildEntityIdFromInfos(Matchers.any(Difficulte.class), Matchers.anyLong());

        // Act
        saveAll(liste);
        for (int i = 0; i < 5; i++) {
            Personne p = personneDao.getRandomByDifficulte(null);

            // Assert
            assertThat(p).isSameAs(liste.get((int) idBuilder.getLastId() - 1));
        }
    }

    /*
     * 
     * Méthodes utilitaires
     */
    private void saveAll(final List<Personne> liste) {
        for (Personne personne : liste) {
            personneDao.saveImmediate(personne);
        }
    }

    private List<Personne> generatePersonnes(final int nombre) {
        List<Personne> liste = Lists.newArrayList();
        for (int i = 0; i < nombre; i++) {
            Personne p = new Personne();
            p.setNom("Random" + i + RandomStringUtils.randomAlphabetic(20));
            p.setPrenom("Random" + i + RandomStringUtils.randomAlphabetic(20));
            p.setDifficulte(Difficulte.SIMPLE);
            liste.add(p);
        }
        return liste;
    }

    private class MockIdGenerator implements Answer<Long> {
        private long id = 0;

        @Override
        public Long answer(final InvocationOnMock invocation) throws Throwable {
            return ++id;
        }
    }

    private class MockIdBuilder implements Answer<Long> {

        long lastId;

        @Override
        public Long answer(final InvocationOnMock invocation) throws Throwable {
            lastId = (long) invocation.getArguments()[1];
            return lastId;
        }

        public long getLastId() {
            return lastId;
        }
    }

}
