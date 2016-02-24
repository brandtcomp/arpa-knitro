/*******************************************************/
/* Copyright (c) 2015 by Artelys                       */
/* All Rights Reserved                                 */
/*******************************************************/

package com.artelys.knitro.examples;


import java.util.Arrays;

import com.artelys.knitro.api.KTRConstants;
import com.artelys.knitro.api.KTRException;
import com.artelys.knitro.api.KTRSolver;
import com.artelys.knitro.examples.Problems.ProblemHS15;
import com.artelys.knitro.examples.Problems.ProblemQCQP;
import com.artelys.knitro.examples.callbacks.ExampleMSInitPtCallback;
import com.artelys.knitro.examples.callbacks.ExampleMSProcessCallback;
import com.artelys.knitro.examples.callbacks.ExampleOutputRedirection;


/**
 * A simple example of using this API to define and solve problems with or without user-defined parameters.<br/>
 * Solves HS15 and QCQP examples.
 */
public class ExampleSolver
{

    public static void main(String[] args) throws KTRException {
        runHS15();
        runQCQP();
    }

    /** A very simple example running HS15 problem with no user option */
    private static void runHS15() throws KTRException {
        // Instanciate problem
        ProblemHS15 instance = new ProblemHS15();

        // Create new solver
        KTRSolver solver = new KTRSolver(instance);

        // Solve problem
        solver.solve();
    }

    /** A simple example solving a QCQP with a few user options and user-defined callback for init point.<br/>
     * Optimization is performed using forward gradient evaluation and finite differences.
     * The multi-start is enabled with user-defined initial point and callback on each solution.
     * Finally, Knitro output is redirected to another callback (writing it to standard error). */
    private static void runQCQP() throws KTRException
    {
        // Instantiate problem
        ProblemQCQP instance = new ProblemQCQP();

        // ===== Define additional callbacks ===== //

        // Set user defined callback for multi-start init point
        ExampleMSInitPtCallback initPtCallback = new ExampleMSInitPtCallback(0,5000);
        instance.setMSInitPtCallback(initPtCallback);

        // Set a callback which is called after each multi-start solve
        ExampleMSProcessCallback processCallback = new ExampleMSProcessCallback();
        instance.setMSProcessCallback(processCallback);

        // Redirect Knitro output to standard error
        instance.setPutStringFunction(new ExampleOutputRedirection());

        // ===== Create and solve instance ===== //
        // Create a new solver and set it to use forward finite-differences to compute the gradient
        // and BFGS for the hessian
        KTRSolver solver = new KTRSolver(instance, KTRConstants.KTR_GRADOPT_FORWARD, KTRConstants.KTR_HESSOPT_BFGS);

        // Set additional parameters

        // Set multi-start parameter to true and tell knitro to use user-defined callback for initial points
        solver.useMSInitptCallback();

        // Call post solve callback
        solver.useMSProcessCallback();

        // ===== Solve instance ===== //
        solver.solve();

        // ===== Modify variable bounds and callback parameters and resolve ===== //
        // Disable callback verbosity
        initPtCallback.setVerbosity(false);
        processCallback.setEnabled(false);

        // Modify bounds: makes previous best solution infeasible
        instance.setVarUpBnds(7.0);

        // Solve
        solver.solve();
    }
}
