package Team_REAP.appserver.RAG.AI.service;

import Team_REAP.appserver.RAG.AI.model.Answer;
import Team_REAP.appserver.RAG.AI.model.Question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    //todo 이거 왜 null로 뜨냐
//    @Value("classpath:templates/defaultPrompt.st")
//    private Resource defaultTemplate;

    public ChatService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.defaultSystem(
                "다음과 같은 맥락을 사용하여 마지막 질문에 대답하세요." +
                "만약 답을 모르면 모른다고만 말하고 답을 지어내려고 하지 마세요." +
                "답변은 최대 4문장으로 대답하되, 가능한 간결하게 유지하세요.").build();
        this.vectorStore=vectorStore;
    }
    public Answer generateChatResponse(Question question){
        //답변을 실시간으로 전달하기
//        Flux<String> answer=chatClient.prompt()
//                .user(question.question())
//                .advisors(new QuestionAnswerAdvisor(vectorStore))
//                .stream()
//                .content();

        //답변이 다 완성되고 난 후 전달하기
        String answer=chatClient.prompt()
                .user(question.question())
                .advisors(new QuestionAnswerAdvisor(vectorStore))
                .call()
                .content();

        return new Answer(answer);
    }
}
