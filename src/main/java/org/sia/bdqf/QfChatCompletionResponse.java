package org.sia.bdqf;

import com.plexpt.chatgpt.entity.billing.Usage;
import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/19 21:01
 */
@Data
public class QfChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private long sentence_id;
    private Boolean is_end;
    private Boolean is_truncated;
    private Boolean need_clear_history;
    private long ban_round;
    private String result;

    private Usage usage;
}
