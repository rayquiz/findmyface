package fr.rayquiz.findmyface.dao.impl;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Test;

import fr.rayquiz.findmyface.bo.Difficulte;
import fr.rayquiz.findmyface.dao.IIdGeneratorService;
import fr.rayquiz.findmyface.tests.GaeDefaultTestClass;

public class IdGeneratorServiceTest extends GaeDefaultTestClass {

    private IIdGeneratorService service;

    @Override
    public void setUp() {
        super.setUp();
        service = new IdGeneratorService();
    }

    @Test
    public void should_increment_and_get() {
        // Arrange

        // Act
        for (long i = 1; i < 10; i++) {
            long newId = service.generateNewId(Difficulte.SIMPLE);
            long value = service.getCount(Difficulte.SIMPLE);

            // Assert
            assertThat(newId).isSameAs(Difficulte.SIMPLE.getImportance() * 10 + i);
            assertThat(value).isSameAs(i);
        }

        // Act
        for (long i = 10; i < 20; i++) {
            long newId = service.generateNewId(Difficulte.SIMPLE);
            long value = service.getCount(Difficulte.SIMPLE);

            // Assert
            assertThat(newId).isSameAs(Difficulte.SIMPLE.getImportance() * 100 + i);
            assertThat(value).isSameAs(i);
        }
    }

    @Test
    public void should_increment_and_get_multiple_shards() {
        // Arrange

        // Act
        long moyFirst = service.generateNewId(Difficulte.MOYEN);
        long difFirst = service.getCount(Difficulte.DIFFICILE);

        long moySecond = service.generateNewId(Difficulte.MOYEN);
        long difSecond = service.getCount(Difficulte.DIFFICILE);

        service.generateNewId(Difficulte.DIFFICILE);
        long difThird = service.getCount(Difficulte.DIFFICILE);

        // Assert
        assertThat(moyFirst).isNotEqualTo(moySecond);
        assertThat(difFirst).isEqualTo(difSecond).isEqualTo(0);
        assertThat(difThird).isEqualTo(1);
    }
}
