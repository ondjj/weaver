package com.task.weaver.domain.comment.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RequestCreateComment
        (
                UUID writerId,
                Long issueId,
                String body,
                LocalDateTime date
        ) {
}