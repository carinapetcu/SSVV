package integration;

import domain.Nota;
import domain.Student;
import domain.Tema;
import org.junit.Before;
import org.junit.Test;
import repository.NotaXMLRepo;
import repository.StudentXMLRepo;
import repository.TemaXMLRepo;
import service.Service;
import validation.NotaValidator;
import validation.StudentValidator;
import validation.TemaValidator;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class IntegrationTest {
    private final static String FILENAME_STUDENT = "fisiere/Studenti_integration.xml";
    private final static String FILENAME_TEMA = "fisiere/Teme_integration.xml";
    private final static String FILENAME_NOTA = "fisiere/Note_integration.xml";

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
    public void test_AddTema_IdValidAndNotExistent_TemaAdded() {
        final Tema tema = new Tema("100", "abc", 3, 1);
        final Tema result = service.addTema(tema);

        assertNull(result);

        service.deleteTema("100");
    }

    @Test
    public void test_AddNota_AllIdsValidAndNotaIdNotExistent_NotaAdded() {
        final Nota nota = new Nota("100", "1", "1", 10, LocalDate.of(2018, 10, 7));
        final double result = service.addNota(nota, "very good");

        assertEquals(10, result, 0.0);

        service.deleteNota("100");
    }

    @Test
    public void test_IntegrationNotaTemaStudent() {
        final Student student = new Student("100", "a", 0, "a@scs.ubbcluj.ro");
        final Tema tema = new Tema("100", "abc", 3, 1);
        final Nota nota = new Nota(null, "100", "100", 10, LocalDate.of(2018, 10, 7));

        service.addStudent(student);
        service.addTema(tema);

        final double result = service.addNota(nota, "very good");

        assertEquals(10, result, 0.0);

        service.deleteNota("100");
        service.deleteTema("100");
        service.deleteStudent("100");
    }
}
