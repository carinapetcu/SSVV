package service;

import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;
import validation.ValidationException;

import static org.junit.Assert.*;

public class ServiceTest {
    private final static String FILENAME_STUDENT = "fisiere/Studenti.xml";
    private final static String FILENAME_TEMA = "fisiere/Teme.xml";
    private final static String FILENAME_NOTA = "fisiere/Note.xml";

    private static Service service;

    @Before
    public void setup() {
        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(FILENAME_STUDENT);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(FILENAME_TEMA);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(FILENAME_NOTA);

        service = new Service(studentXMLRepository,
                studentValidator,
                temaXMLRepository,
                temaValidator,
                notaXMLRepository,
                notaValidator);
    }

    @Test
    public void test_AddStudent_IdValidAndNotExistent_StudentAdded() {
        final Student student = new Student("100", "a", 0, "a@scs.ubbcluj.ro");
        final Student result = service.addStudent(student);

        assertNull(result);

        service.deleteStudent("100");
    }

    @Test
    public void test_AddStudent_ExistentId_NothingChanges() {
        final Student student = new Student("100", "a", 1, "a@scs.ubbcluj.ro");
        service.addStudent(student);
        final Student result = service.addStudent(student);

        assertNotNull(result);
        assertEquals("100", result.getID());

        service.deleteStudent("100");
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_EmptyIdStudent_ThrowsErrorMessage() {
        final Student student = new Student("", "a", 0, "a@scs.ubbcluj.ro");

        service.addStudent(student);
    }

    @Test(expected=NullPointerException.class)
    public void test_AddStudent_NullIdStudent_ThrowsErrorMessage() {
        final Student student = new Student(null, "a", 0, "a@scs.ubbcluj.ro");

        service.addStudent(student);
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_EmptyName_ThrowsErrorMessage() {
        final Student student = new Student("100", "", 0, "a@scs.ubbcluj.ro");

        service.addStudent(student);
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_NullName_ThrowsErrorMessage() {
        final Student student = new Student("100", null, 0, "a@scs.ubbcluj.ro");

        service.addStudent(student);
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_EmptyEmail_ThrowsErrorMessage() {
        final Student student = new Student("100", "a", 0, "");

        service.addStudent(student);
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_NullEmail_ThrowsErrorMessage() {
        final Student student = new Student("100", "a", 0, null);

        service.addStudent(student);
    }

    @Test(expected=ValidationException.class)
    public void test_AddStudent_NegativeGroup_ThrowsErrorMessage() {
        final Student student = new Student("100", "a", -1, "a@scs.ubbcluj.ro");

        service.addStudent(student);
    }

    @Test
    public void test_AddTema_IdValidAndNotExistent_TemaAdded() {
        final Tema tema = new Tema("100", "abc", 3, 1);
        final Tema result = service.addTema(tema);

        assertNull(result);

        service.deleteTema("100");
    }

    @Test
    public void test_AddTema_IdValidAndExistent_NothingChanges() {
        final Tema tema = new Tema("1", "abc", 3, 1);
        final Tema result = service.addTema(tema);

        assertNotNull(result);
        assertEquals("1", result.getID());
    }

    @Test(expected=ValidationException.class)
    public void test_AddTema_EmptyInvalidId_ThrowsErrorMessage() {
        final Tema tema = new Tema("", "abc", 3, 1);

        service.addTema(tema);
    }

    @Test(expected=ValidationException.class)
    public void test_AddTema_EmptyInvalidDescriere_ThrowsErrorMessage() {
        final Tema tema = new Tema("1", "", 3, 1);

        service.addTema(tema);
    }

    @Test(expected=ValidationException.class)
    public void test_AddTema_InvalidDeadline_ThrowsErrorMessage() {
        final Tema tema = new Tema("1", "abc", -1, 1);

        service.addTema(tema);
    }

    @Test(expected=ValidationException.class)
    public void test_AddTema_InvalidPrimire_ThrowsErrorMessage() {
        final Tema tema = new Tema("1", "abc", 3, 15);

        service.addTema(tema);
    }
}
