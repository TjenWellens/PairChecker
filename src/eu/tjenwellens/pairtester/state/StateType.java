package eu.tjenwellens.pairtester.state;

import eu.tjenwellens.pairtester.RatedPairI;

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

    public boolean correct(RatedPairI pair)
    {
        return state.correct(pair);
    }

    public boolean wrong(RatedPairI pair)
    {
        return state.wrong(pair);
    }

    public boolean skip(RatedPairI pair)
    {
        return state.skip(pair);
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
        public boolean correct(RatedPairI pair)
        {
            return false;
        }

        public boolean wrong(RatedPairI pair)
        {
            return false;
        }

        public boolean skip(RatedPairI pair)
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
        public boolean correct(RatedPairI pair)
        {
            return false;
        }

        public boolean wrong(RatedPairI pair)
        {
            return false;
        }

        public boolean skip(RatedPairI pair)
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
        public boolean correct(RatedPairI pair)
        {
            pair.correct();
            return true;
        }

        public boolean wrong(RatedPairI pair)
        {
            pair.wrong();
            return true;
        }

        public boolean skip(RatedPairI pair)
        {
            pair.skip();
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
