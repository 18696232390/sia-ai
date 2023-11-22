package org.sia.vo;

import lombok.Data;

/**
 * @Description:
 * @Author: 高灶顺
 * @CreateDate: 2023/9/13 19:08
 */
@Data
public class AdminImageProxyInfoVo {
    private String discordApi = "https://discord.com/api";
    private String discordWss = "wss://gateway.discord.gg";

    private String discordWssProxy;
    private String discordApiProxy;
}
