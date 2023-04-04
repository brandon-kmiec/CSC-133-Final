package Data;

import java.util.StringTokenizer;

public class Command {
    // Fields
    private String cmd;
    private String[] params;

    // Constructor
    public Command(String raw) {
        StringTokenizer st = new StringTokenizer(raw, ":");
        if (st.countTokens() < 2) return;
        cmd = st.nextToken().trim();
        String rawParams = st.nextToken().trim();
        StringTokenizer st2 = new StringTokenizer(rawParams, ",");
        params = new String[st2.countTokens()];
        for (int i = 0; i < params.length; i++)
            params[i] = st2.nextToken().trim();
    }

    // Methods
    public boolean isCommand(String input) {
        if (cmd == null) return false;
        return cmd.toLowerCase().equals(input.toLowerCase());
    }

    public String[] getParams() {
        return params;
    }

    public int getNumParams() {
        if (params == null) return 0;
        return params.length;
    }

    public String getParamByIndex(int index) {
        if (params == null) return null;
        if (index >= params.length) return null;
        return params[index];
    }
}
