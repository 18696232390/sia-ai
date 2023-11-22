package org.sia.vo.request;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/10/20 10:07
 */
@Data
public class AiProxyCreateApiKeyReqVo {
    private String name;
    private Boolean enableSubPointAccount = true;
    private Boolean enableSearchBing = true;
    private Boolean enableSearchGoogle = true;
    private String initPoint = "1";
    private String[] modelPermission = new String[]{
            "claude-2",
            "claude-instant-1",
            "gpt-3.5-turbo",
            "gpt-3.5-turbo-instruct",
            "gpt-3.5-turbo-0301",
            "gpt-3.5-turbo-0613",
            "gpt-3.5-turbo-16k",
            "gpt-3.5-turbo-16k-0613",
            "gpt-4",
            "gpt-4-0314",
            "gpt-4-0613",
            "gpt-4-32k",
            "text-davinci-003",
            "text-davinci-002",
            "text-curie-001",
            "text-babbage-001",
            "text-ada-001",
            "text-embedding-ada-002",
            "text-search-ada-doc-001",
            "whisper-1",
            "text-davinci-edit-001",
            "code-davinci-edit-001"
    };
    private Boolean managementKey = false;
    private String externalId;
}
