package service;

import domain.Student;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

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
        final Student student = new Student("100", "a", 936, "a@scs.ubbcluj.ro");
        service.deleteStudent("100");
        final Student result = service.addStudent(student);

        assertNull(result);
    }

    @Test
    public void test_AddStudent_ExistentId_NothingChanges() {
        final Student student = new Student("100", "a", 936, "a@scs.ubbcluj.ro");
        service.deleteStudent("100");
        service.addStudent(student);
        final Student result = service.addStudent(student);

        assertNotNull(result);
        assertEquals("100", result.getID());
    }
}
