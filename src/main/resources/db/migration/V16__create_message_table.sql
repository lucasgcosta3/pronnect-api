CREATE TABLE message (
    id UUID PRIMARY KEY,

    conversation_id UUID NOT NULL,
    sender_id UUID NOT NULL,

    content TEXT NOT NULL,

    created_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_message_conversation
        FOREIGN KEY (conversation_id)
        REFERENCES conversation(id),

    CONSTRAINT fk_message_sender
        FOREIGN KEY (sender_id)
        REFERENCES account(id)
);