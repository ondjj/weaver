package com.task.weaver.domain.chattingRoomMember;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;
import com.task.weaver.domain.websocket.entity.ChattingRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "CHATTING_ROOM_MEMBER")
@NoArgsConstructor
@AllArgsConstructor
public class ChattingRoomMember extends BaseEntity {

    @Id
    @Column(name = "chattingRoom_member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chattingRoomMemberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chattingRoom_id")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private UserOauthMember member;
}
