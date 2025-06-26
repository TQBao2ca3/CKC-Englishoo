package com.example.ckc_englihoo.Screen.Exercises.MultipleChoiceComponents

data class MultipleChoiceQuestion(
    val id: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: String,
    val explanation: String
)

fun getSampleMultipleChoiceQuestions(): List<MultipleChoiceQuestion> {
    return listOf(
        MultipleChoiceQuestion(
            id = "1",
            question = "What is the capital of Vietnam?",
            options = listOf("Ho Chi Minh City", "Hanoi", "Da Nang", "Hue"),
            correctAnswer = "Hanoi",
            explanation = "Hanoi is the capital and second-largest city of Vietnam."
        ),
        MultipleChoiceQuestion(
            id = "2",
            question = "Which word is a noun?",
            options = listOf("Beautiful", "Quickly", "House", "Run"),
            correctAnswer = "House",
            explanation = "A noun is a word that names a person, place, thing, or idea."
        ),
        MultipleChoiceQuestion(
            id = "3",
            question = "Choose the correct past tense of 'go':",
            options = listOf("Goed", "Went", "Gone", "Going"),
            correctAnswer = "Went",
            explanation = "The past tense of 'go' is 'went'."
        ),
        MultipleChoiceQuestion(
            id = "4",
            question = "What does 'Hello' mean in Vietnamese?",
            options = listOf("Tạm biệt", "Xin chào", "Cảm ơn", "Xin lỗi"),
            correctAnswer = "Xin chào",
            explanation = "'Hello' means 'Xin chào' in Vietnamese."
        ),
        MultipleChoiceQuestion(
            id = "5",
            question = "Which sentence is correct?",
            options = listOf(
                "She don't like apples",
                "She doesn't like apples",
                "She not like apples",
                "She no like apples"
            ),
            correctAnswer = "She doesn't like apples",
            explanation = "Use 'doesn't' with third person singular subjects."
        ),
        MultipleChoiceQuestion(
            id = "6",
            question = "What is the plural form of 'child'?",
            options = listOf("Childs", "Children", "Childes", "Childrens"),
            correctAnswer = "Children",
            explanation = "'Children' is the irregular plural form of 'child'."
        ),
        MultipleChoiceQuestion(
            id = "7",
            question = "Choose the correct article:",
            options = listOf("A apple", "An apple", "The apple", "Some apple"),
            correctAnswer = "An apple",
            explanation = "Use 'an' before words that start with a vowel sound."
        ),
        MultipleChoiceQuestion(
            id = "8",
            question = "What does 'Thank you' mean in Vietnamese?",
            options = listOf("Xin chào", "Tạm biệt", "Cảm ơn", "Xin lỗi"),
            correctAnswer = "Cảm ơn",
            explanation = "'Thank you' means 'Cảm ơn' in Vietnamese."
        ),
        MultipleChoiceQuestion(
            id = "9",
            question = "Which is the correct present continuous form?",
            options = listOf("I am run", "I am running", "I running", "I am runs"),
            correctAnswer = "I am running",
            explanation = "Present continuous uses 'am/is/are + verb-ing'."
        ),
        MultipleChoiceQuestion(
            id = "10",
            question = "Choose the correct comparative form of 'good':",
            options = listOf("Gooder", "More good", "Better", "Best"),
            correctAnswer = "Better",
            explanation = "'Better' is the irregular comparative form of 'good'."
        )
    )
}
