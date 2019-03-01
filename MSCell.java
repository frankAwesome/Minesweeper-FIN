public class MSCell
{
	private boolean revealed;
    private boolean bomb;
    private int value;
    private boolean flagged;
	
	MSCell()
	{
		revealed = false;
		bomb = false;
		value = 0;
		flagged = false;
	}
	
	MSCell(boolean revealed, boolean bomb, int value, boolean flagged)
	{
		this.revealed = revealed;
		this.bomb = bomb;
		this.value = value;
		this.flagged = flagged;
	}
	
	public void setRevealed(boolean revealed)
	{
		this.revealed = revealed;
	}
	
    public boolean isRevealed()
	{
		return revealed;
	}
	
    public void setFlagged(boolean flagged)
	{
		this.flagged = flagged;
	}
	
    public boolean isFlagged()
	{
		return flagged;
	}
	
	public void setBomb(boolean bomb)
	{
		this.bomb = bomb;
	}
	
    public boolean isBomb()
	{
		return bomb;
	}
	
    public void setValue(int v)
	{
		this.value = v;
	}
	
    public int getValue()
	{
		return value;
	}
	
    public String toString()
	{
		String returnString = "empty";
		
		if (bomb == true)
		{
		   returnString = "B";
		}

		else
	    {
			returnString = ""+value;
		}
		
		if (flagged == true)
		{
			returnString = "!";
		}
		
        return returnString;
	}
}