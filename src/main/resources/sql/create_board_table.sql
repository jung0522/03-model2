-- ==========================================
-- Board 및 FileUpload 테이블 생성 스크립트
-- ==========================================

-- ====================
-- 1. 게시글 테이블
-- ====================
DROP TABLE IF EXISTS board CASCADE;

CREATE TABLE board (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       content CLOB NOT NULL,
                       author BIGINT NOT NULL,          -- FK: users.id
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       CONSTRAINT fk_board_user FOREIGN KEY (author) REFERENCES users(id) ON DELETE CASCADE
);

-- 인덱스
CREATE INDEX IF NOT EXISTS idx_board_author ON board(author);
CREATE INDEX IF NOT EXISTS idx_board_created_at ON board(created_at);

-- ====================
-- 2. 첨부파일 테이블
-- ====================
CREATE TABLE IF NOT EXISTS file_upload (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           board_id BIGINT NOT NULL,
                                           file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    uploaded_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
    );

-- 인덱스
CREATE INDEX IF NOT EXISTS idx_file_board_id ON file_upload(board_id);
