package top.oupanyu.Functions.openai;

import java.util.ArrayList;
import java.util.List;

/*{"id":"chatcmpl-75dJRnE5dtUfZsSiQmBy27Zjs0B4n",
"object":"chat.completion",
"created":1681577113,
"model":"gpt-3.5-turbo-0301",
"usage":{"prompt_tokens":10,"completion_tokens":10,"total_tokens":20},
"choices":[{"message":{"role":"assistant","content":"Hello there! How can I assist you today?"},"finish_reason":"stop","index":0}]}
*/
public class OpenAIResponse {
    public String id;
    public String object;
    //public Long created;
    public String model;
    public Usage usage;
    public List<Choice> choices;
}
class Usage{
    public int prompt_tokens;
    public int completion_tokens;
    public int total_tokens;
}
class Choice{
    public Message message;
    public String finish_reason;
    public int index;
}
class Message{
    public String role;
    public String content;
}
