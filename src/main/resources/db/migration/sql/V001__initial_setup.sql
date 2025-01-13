CREATE TABLE IF NOT EXISTS video(
    id              UUID                    NOT NULL,
    user_id         UUID                    NOT NULL,
    name            TEXT                    NOT NULL,
    status          TEXT                    NOT NULL,
    created_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,
    input_path      TEXT                    NOT NULL,
    output_path     TEXT,
    metadata        JSONB                   NOT NULL,

    CONSTRAINT pkey_video PRIMARY KEY (id)
);

CREATE INDEX idx_video_user_id ON video(user_id);
