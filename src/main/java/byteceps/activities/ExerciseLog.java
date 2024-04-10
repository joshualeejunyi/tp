package byteceps.activities;

import java.util.List;
public class ExerciseLog extends Activity {
    private final List<Integer> weights;
    private final int sets;
    private final List<Integer> repetitions;

    public ExerciseLog(String activityName, List<Integer> weights, int sets, List<Integer> repetitions) {
        super(activityName);
        this.weights = weights;
        this.sets = sets;
        this.repetitions = repetitions;
    }


    public int getSets() {
        return sets;
    }

    public List<Integer> getRepetitions() {
        return repetitions;
    }

    public List<Integer> getWeights() {
        return weights;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return false;
    }
}
