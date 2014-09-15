describe('CreateQuizController', function(){
	beforeEach(module('QuizApp'));

  	var ctrl, scope;

  	var QuizAPIMock = (function(){
  		return {
  			create_quiz: function(options){
  				options.done();
  			}
  		}
  	})();

  	beforeEach(inject(function($controller, $rootScope) {
    	scope = $rootScope.$new();
    	ctrl = $controller('CreateQuizController', {
      		$scope: scope,
      		QuizAPI: QuizAPIMock
    	});
    }));

  	it('should be initialized correctly', function(){
  		expect(scope.quiz.title).toBe('');
  		expect(scope.quiz.items.length).toBe(0);
  	});

  	it('should be able to add an open question item', function(){
      scope.add_open_question();
      expect(scope.quiz.items[0].item_type).toBe('open_question');

  		scope.quiz.items[0].question = 'Wazzup?';

  		expect(scope.quiz.items.length).toBe(1);
  		expect(scope.quiz.items[0].question).toBe('Wazzup?')
  		expect(scope.quiz.items[0].item_type).toBe('open_question');
  	});

    it('should be able to add an text container item', function(){
      scope.add_text_container();
      expect(scope.quiz.items[0].item_type).toBe('text_container');

      scope.quiz.items[0].content = '<h1>This is my quiz!</h1>';

      expect(scope.quiz.items.length).toBe(1);
      expect(scope.quiz.items[0].content).toBe('<h1>This is my quiz!</h1>');
    });

    it('should be able to add an multiple choice question item', function(){
      scope.add_multiple_choice_question();
      expect(scope.quiz.items[0].item_type).toBe('multiple_choice_question');

      scope.quiz.items[0].question = 'Pick one!';

      expect(scope.quiz.items.length).toBe(1);
      expect(scope.quiz.items[0].question).toBe('Pick one!');
    });

    it('should be able to add an option to a multiple choice question', function(){
      scope.add_multiple_choice_question();
      scope.quiz.items[0].new_option = {
        title: 'Pick me!'
      }

      scope.add_option(scope.quiz.items[0]);

      expect(scope.quiz.items[0].options.length).toBe(1);
      expect(scope.quiz.items[0].options[0].title).toBe('Pick me!');
    });

    it('should be able to remove an option from a multiple_choice_question', function(){
      scope.add_multiple_choice_question();
      scope.quiz.items[0].new_option = {
        title: 'Pick me!'
      }

      scope.add_option(scope.quiz.items[0]);

      expect(scope.quiz.items[0].options.length).toBe(1);

      scope.remove_option(scope.quiz.items[0], 0);

      expect(scope.quiz.items[0].options.length).toBe(0);      
    });

    it('should be able to remove an item', function(){
      scope.add_text_container();

      expect(scope.quiz.items.length).toBe(1);

      scope.add_open_question();

      expect(scope.quiz.items.length).toBe(2);

      scope.remove_item(0);

      expect(scope.quiz.items.length).toBe(1);

      scope.remove_item(0);

      expect(scope.quiz.items.length).toBe(0);      
    });

  	it('should have items ordered correctly when added', function(){
  		scope.add_open_question();
      scope.quiz.items[0].question = 'Wazzup?';

      scope.add_open_question();
      scope.quiz.items[1].question = 'What is love?';

  		expect(scope.quiz.items[0].question).toBe('Wazzup?');
      expect(scope.quiz.items[1].question).toBe('What is love?');
  	});

  	it('should be able to save a quiz and inform the user', function(){
  		scope.quiz = {
  			title: 'My awesome quiz',
  			items: []
  		}

  		scope.save_quiz();

  		expect(scope.message.type).toBe('success');
  		expect(scope.message.content).toBe('The quiz has been saved!');
  	});
});