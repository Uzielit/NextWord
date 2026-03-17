package com.nextword.backend.feature.messaging.repository;

import com.nextword.backend.feature.messaging.entity.Message;
import com.nextword.backend.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Message, String> {

    List<Message> findBySenderIdAndReceiverIdOrSenderIdAndReceiverIdOrderBySentAtAsc(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

}
