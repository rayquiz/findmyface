package fr.rayquiz.findmyface.dao.impl;

import org.junit.Test;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.googlecode.objectify.ObjectifyService;

import fr.rayquiz.findmyface.dao.bo.JoueurInfosBo;
import fr.rayquiz.findmyface.exceptions.NotLoggedInException;
import fr.rayquiz.findmyface.tests.GaeDefaultTestClass;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class JoueurInfosDaoTest extends GaeDefaultTestClass {

    private JoueurInfosDao joueurInfosDao;
    private UserService userService;

    @Override
    public void setUp() {
        super.setUp();
        joueurInfosDao = new JoueurInfosDao();
        userService = mock(UserService.class);
        joueurInfosDao.setUserService(userService);
    }

    @Test(expected = NotLoggedInException.class)
    public void should_throw_not_logged_exception() throws NotLoggedInException {
        // Arrange
        when(userService.isUserLoggedIn()).thenReturn(false);

        // Act
        joueurInfosDao.getCurrentJoueurInfos();
    }

    @Test()
    public void should_get_new_dao() throws NotLoggedInException {
        // Arrange
        User mockUser = new User("test@test.com", "@test.com", "Identifiant");
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(userService.isUserLoggedIn()).thenReturn(true);

        // Act
        JoueurInfosBo joueur = joueurInfosDao.getCurrentJoueurInfos();

        // Assert
        assertThat(joueur).isNotNull().isNotEqualTo(mockUser);
        assertThat(joueur.getId()).isEqualTo(mockUser.getUserId());
        assertThat(joueur.getIdTrouveListe()).isEmpty();
    }

    @Test()
    public void should_get_existing_bo() throws NotLoggedInException {
        // Arrange
        User mockUser = new User("test@test.com", "@test.com", "Identifiant");
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(userService.isUserLoggedIn()).thenReturn(true);
        JoueurInfosBo arrangeUtilisateur = new JoueurInfosBo(mockUser.getUserId());
        arrangeUtilisateur.getIdTrouveListe().add("123456");

        // Act
        ObjectifyService.ofy().save().entity(arrangeUtilisateur).now();
        JoueurInfosBo joueur = joueurInfosDao.getCurrentJoueurInfos();

        // Assert
        assertThat(joueur).isNotNull().isEqualTo(arrangeUtilisateur);
    }
}
