package com.nextword.backend.feature.messaging.repository;

import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, String> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

    @Query("SELECT m FROM Message m WHERE m.senderId = :userId OR m.receiverId = :userId ORDER BY m.sentAt DESC")
    List<Message> findAllMessagesForUser(@Param("userId") String userId);

    @Query("SELECT COUNT(m) FROM Message m WHERE m.receiverId = :myId AND m.senderId = :contactId AND m.read = '0'")
    int countUnreadMessages(@Param("myId") String myId, @Param("contactId") String contactId);
}
