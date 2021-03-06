package app.services;

import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.domain.User;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class QuizServiceTest {
    @InjectMocks
    private QuizService qs;
    
    @Mock
    private QuizRepository qr;
    
    @Mock
    private QuizAnswerRepository qar;
    
    public QuizServiceTest() {
        qs = new QuizService();
        qr = mock(QuizRepository.class);
        qar = mock(QuizAnswerRepository.class);
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testValidateAnswerQuizCombination() {
        when(qr.findOne(1L)).thenReturn(null);
        when(qar.findOne(1L)).thenReturn(null);
        
        boolean thrown = false;
        try {
            qs.validateAnswerQuizCombination(1L, 1L);
        } catch (Exception e) {
            thrown = true;
        }
        
        assertTrue(thrown);
    }

    @Test
    public void testIsValidAnswerQuizCombinationWithNonExistentQuizAndAnswer() {
        when(qr.findOne(1L)).thenReturn(null);
        when(qar.findOne(1L)).thenReturn(null);
        assertFalse(qs.isValidAnswerQuizCombination(1L, 1L));
    }

    @Test
    public void testIsValidAnswerQuizCombinationWithWrongAnswerId() {
        Quiz q1 = mock(Quiz.class);
        when(q1.getId()).thenReturn(1L);
        
        Quiz q2 = mock(Quiz.class);
        when(q2.getId()).thenReturn(2L);
        
        QuizAnswer qa = mock(QuizAnswer.class);
        when(qa.getQuiz()).thenReturn(q2);
        
        when(qr.findOne(1L)).thenReturn(q1);
        when(qar.findOne(1L)).thenReturn(qa);
        
        assertFalse(qs.isValidAnswerQuizCombination(1L, 1L));
    }

//    @Test
//    public void testSubmitAnswer() {
//    }
    
    @Test
    public void testGetAnswersForReview() {
        qs.getAnswersForReview(new Quiz(), new User(), 4);
        verify(this.qar, times(1)).findQuizzesToReview(
                any(Quiz.class), 
                any(User.class), 
                any(PageRequest.class));
    }
//
//    @Test
//    public void testGetReviewsByAnswer() {
//    }
//
//    @Test
//    public void testGetQuizForUsername() {
//    }
//
//    @Test
//    public void testAddPlaceHolderAnswer() {
//    }
//
//    @Test
//    public void testGetPlaceholderAnswers() {
//    }
//
//    @Test
//    public void testDeleteAnswer() {
//    }
    
}
