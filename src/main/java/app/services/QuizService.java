package app.services;

import app.domain.PeerReview;
import app.domain.Quiz;
import app.domain.QuizAnswer;
import app.repositories.PeerReviewRepository;
import app.repositories.QuizAnswerRepository;
import app.repositories.QuizRepository;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class QuizService {
    @Autowired
    private QuizAnswerRepository answerRepo;
    
    @Autowired
    private QuizRepository quizRepo;
    
    @Autowired
    private PeerReviewRepository reviewRepo;
    
    public List<QuizAnswer> sumbitAnswer(QuizAnswer answer, Long quizId) {
        Quiz q = quizRepo.findOne(quizId);
        answer.setQuiz(q);
        answer = answerRepo.save(answer);
        
        if (q.isReviewable()) {
            return getAnswersForReview(q, answer.getUser());
        } else {
            return null;
        }
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, String user) {
        int answerCount = 2;
        
        if (quiz.getQuizAnswers().size() >= answerCount) {
            return getAnswersForReview(quiz, user, answerCount);
        } else {
            Gson gson = new Gson();
            return gson.fromJson(quiz.getPlaceholderAnswers(), List.class);
        }
    }
    
    public List<QuizAnswer> getAnswersForReview(Quiz quiz, String user, int answerCount) {
        Pageable pageable = new PageRequest(0, answerCount);
        List<QuizAnswer> qas = answerRepo.findByQuizAndUserNotOrderedByReviewCount(quiz, user, pageable);
        
        return qas;
    }
    
    public List<PeerReview> getReviewsForAnAnswer(Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        return reviewRepo.findByQuizAnswer(qa);
    }
    
    public PeerReview saveNewReview(PeerReview review, Long answerId, Long quizId) {
        if (!isValidAnswerQuizCombination(answerId, quizId)) 
            throw new IllegalArgumentException("bad answerId, quizId combination!");
        
        QuizAnswer qa = answerRepo.findOne(answerId);
        review.setQuizAnswer(qa);
        PeerReview newReview = reviewRepo.save(review);
        
        return newReview;
    }
    
    private boolean isValidAnswerQuizCombination(Long answerId, Long quizId) {
        QuizAnswer qa = answerRepo.findOne(answerId);
        Quiz q = quizRepo.findOne(quizId);
        
        if (qa == null || q == null || qa.getQuiz() == null || !qa.getQuiz().getId().equals(quizId)) {
            return false;
        } else {
            return true;
        }
    }

    public Quiz getQuizForUser(Long id, String user) {
        Quiz q = quizRepo.findOne(id);
        
        if (answerRepo.findByQuizAndUser(q, user).isEmpty()) {
            q.setAnswered(false);
        } else {
            q.setAnswered(true);
        }
            
        return q;
    }
    
    public void addPlaceholderAnswer(QuizAnswer quizAnswer, Long quizId) {
        Quiz quiz = quizRepo.findOne(quizId);
        Gson gson = new Gson();
        
        List<QuizAnswer> placeholderAnswers = gson.fromJson(quiz.getPlaceholderAnswers(), List.class);
        if (placeholderAnswers == null) {
            placeholderAnswers = new ArrayList<QuizAnswer>();
        }
        
        placeholderAnswers.add(quizAnswer);
        quiz.setPlaceholderAnswers(gson.toJson(placeholderAnswers));
    }
}