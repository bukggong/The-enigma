package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import ucb.util.CommandArgs;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Jenna(dayeon)
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            CommandArgs options =
                    new CommandArgs("--verbose --=(.*){1,3}", args);
            if (!options.ok()) {
                throw error("Usage: java enigma.Main [--verbose] "
                        + "[INPUT [OUTPUT]]");
            }

            _verbose = options.contains("--verbose");
            new Main(options.get("--")).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Open the necessary files for non-option arguments ARGS (see comment
     *  on main). */
    Main(List<String> args) {
        _config = getInput(args.get(0));

        if (args.size() > 1) {
            _input = getInput(args.get(1));
        } else {
            _input = new Scanner(System.in);
        }

        if (args.size() > 2) {
            _output = getOutput(args.get(2));
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {

        Machine enigmaMch = readConfig();
        String newline = _input.nextLine();

        if (!newline.contains("*")) {
            throw new EnigmaException("it contains *");
        }
        while (newline != null) {
            if (newline.contains("*")) {
                setUp(enigmaMch, newline);
            } else if (newline.equals("")) {
                _output.println();
            } else {
                newline = newline.replaceAll("\\s", "");
                printMessageLine(enigmaMch.convert(newline));
            }
            if (_input.hasNextLine()) {
                newline = _input.nextLine();
            } else {
                newline = null;
            }

        }

    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.next());

            int rotorNum = _config.nextInt();
            int pawls = _config.nextInt();
            ArrayList<Rotor> rotorAll = new ArrayList<Rotor>();

            if (!_config.hasNext()) {
                throw new EnigmaException("there are no rotors");
            }
            follwoingName = _config.next();
            while (_config.hasNext(".*")) {
                rotorAll.add(readRotor());
            }
            return new Machine(_alphabet, rotorNum, pawls, rotorAll);

        } catch (NoSuchElementException excp) {
            throw error("truncated configuration file");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String title = follwoingName;
            String typeRotor = _config.next();
            if (typeRotor == null || typeRotor.startsWith("(")) {
                throw new EnigmaException("invalid rotor type");
            }

            String cycles = "";
            String later = _config.next();

            while (_config.hasNext() && later.startsWith("(")) {

                cycles += later + " ";
                later = _config.next();
                follwoingName = later;
            }

            if (!_config.hasNext()) {
                cycles += later;
            }
            Permutation p = new Permutation(cycles, _alphabet);
            if (typeRotor.charAt(0) == 'N') {
                return new FixedRotor(title, p);
            }
            if (typeRotor.charAt(0) == 'M') {
                return new MovingRotor(title, p, typeRotor.substring(1));
            } else {
                return new Reflector(title, p);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified ifn the assignment. */
    private void setUp(Machine M, String settings) {

        Scanner setting = new Scanner(settings);
        String[] rt = new String[M.numRotors()];
        setting.next();

        for (int i = 0; i < M.numRotors(); i++) {
            String newRotor = setting.next();
            rt[i] = newRotor;
        }

        M.insertRotors(rt);
        if (!setting.hasNext()) {
            throw new EnigmaException("unavailable rotor setting");
        }
        String settingRo = setting.next();
        if (settingRo.startsWith("(") || settingRo.equals("")) {
            throw new EnigmaException("unavailable rotor setting");
        }

        String next = "";
        String plugboard = "";
        String ring = "";
        if (setting.hasNext()) {
            next = setting.next();
        }
        if (!next.startsWith("(") && !next.equals("")) {
            ring = next;
        } else if (!next.equals("")) {
            plugboard = plugboard.concat(next + " ");
        }
        while (setting.hasNext()) {
            plugboard = plugboard.concat(setting.next() + " ");
        }
        M.setRing(ring);
        M.setRotors(settingRo);
        if (plugboard.length() != 0) {
            M.setPlugboard(new Permutation(plugboard, _alphabet));
        } else {
            M.setPlugboard(new Permutation("", _alphabet));
        }

    }

    /** Return true iff verbose option specified. */
    static boolean verbose() {
        return _verbose;
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        for (int i = 0; i < msg.length(); i += 5) {
            if (i + 5 < msg.length()) {
                _output.print(msg.substring(i, i + 5) + " ");
            } else {
                _output.println(msg.substring(i));
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

    /** True if —verbose specified. */
    private static boolean _verbose;

    /** Rotor name.*/
    private String follwoingName = "";

}
