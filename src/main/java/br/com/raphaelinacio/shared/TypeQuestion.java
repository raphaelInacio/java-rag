package br.com.raphaelinacio.shared;

import dev.langchain4j.model.output.structured.Description;

public enum TypeQuestion {
    @Description("""
            Useful when you need to answer questions about descriptions what the microservices does.
            what technologies its use
            what endpoints or apis its have
            What responsabilities is have
            Why decisisions were does
            Use full question as input.
            """)
    DOCUMENTATION,

    @Description("""
            Useful when you need to answer questions about microservices,
            their dependencies or antipatterns. Also useful for any sort of\s
            aggregation like counting the number of tasks, etc.
            Use full question as input.
            """)
    MICROSERVICE_NAVIGATION,
}
