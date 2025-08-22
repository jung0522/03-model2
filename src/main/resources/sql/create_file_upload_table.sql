DROP TABLE IF EXISTS file_upload CASCADE;

CREATE TABLE file_upload (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- H2에서는 AUTO_INCREMENT 가능
                             board_id BIGINT NOT NULL,
                             original_filename VARCHAR(255) NOT NULL,
                             stored_filename VARCHAR(255) NOT NULL,
                             file_path VARCHAR(500) NOT NULL,
                             file_size BIGINT NOT NULL,
                             content_type VARCHAR(100),
                             upload_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
                             file_type VARCHAR(20),
                             web_url VARCHAR(500),
                             FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
);

-- 인덱스는 테이블 생성 후 별도로 생성
CREATE INDEX idx_board_id ON file_upload(board_id);
CREATE INDEX idx_stored_filename ON file_upload(stored_filename);
CREATE INDEX idx_file_type ON file_upload(file_type);
CREATE INDEX idx_upload_date ON file_upload(upload_date);
