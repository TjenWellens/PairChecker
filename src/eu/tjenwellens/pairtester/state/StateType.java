package eu.tjenwellens.pairtester.state;

/**
 *
 * @author Tjen
 */
public enum StateType implements State
{
    NEW(new NewState()), UNCHECKED(new UncheckedState()), CHECKED(new CheckedState());
    private State state;

    private StateType(State state)
    {
        this.state = state;
    }

    public boolean correct()
    {
        return state.correct();
    }

    public boolean wrong()
    {
        return state.wrong();
    }

    public boolean skip()
    {
        return state.skip();
    }

    public boolean check()
    {
        return state.check();
    }

    public boolean start()
    {
        return state.start();
    }

    private static class NewState implements State
    {
        public boolean correct()
        {
            return false;
        }

        public boolean wrong()
        {
            return false;
        }

        public boolean skip()
        {
            return false;
        }

        public boolean check()
        {
            return false;
        }

        public boolean start()
        {
            return true;
        }
    }

    private static class UncheckedState implements State
    {
        public boolean correct()
        {
            return false;
        }

        public boolean wrong()
        {
            return false;
        }

        public boolean skip()
        {
            return false;
        }

        public boolean check()
        {
            return true;
        }

        public boolean start()
        {
            return false;
        }
    }

    private static class CheckedState implements State
    {
        public boolean correct()
        {
            return true;
        }

        public boolean wrong()
        {
            return true;
        }

        public boolean skip()
        {
            return true;
        }

        public boolean check()
        {
            return false;
        }

        public boolean start()
        {
            return false;
        }
    }
}
