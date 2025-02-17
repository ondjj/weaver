package com.task.weaver.domain.comment.dto.response;

import com.task.weaver.domain.comment.entity.Comment;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ResponseComment(
        UUID commentId,
        String commentBody
) {
    public ResponseComment(Comment comment) {
        this(comment.getCommentId(),comment.getBody());
    }
}
