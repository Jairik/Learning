return {
  "github/copilot.vim",
  config = function()
    -- Disable default Tab mapping
    vim.g.copilot_no_tab_map = true

    -- Proper Tab handling
    vim.keymap.set("i", "<Tab>", function()
      return vim.fn["copilot#Accept"]("\t")
    end, { expr = true, silent = true })
  end,
}
