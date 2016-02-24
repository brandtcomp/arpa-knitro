/*******************************************************/
/* Copyright (c) 2015 by Artelys                       */
/* All Rights Reserved                                 */
/*******************************************************/

package com.artelys.knitro.examples.callbacks;

import com.artelys.knitro.api.KTRException;
import com.artelys.knitro.api.KTRNewptCallback;
import com.artelys.knitro.api.KTRSolver;

import java.util.List;

public class ExampleNewPointCallback extends KTRNewptCallback
{
    @Override
    public int callbackFunction(
            List<Double> x,
            List<Double> lambda,
            double obj,
            List<Double> c,
            List<Double> objGrad,
            List<Double> jac,
            KTRSolver solver) throws KTRException
    {
        System.out.println(">> New point computed by Knitro: (");
        for (int i = 0; i < x.size() - 1; i++) {
            System.out.print(x.get(i) + ", ");
        }

        System.out.println(x.get(x.size() - 1));

        System.out.println("Number FC evals= " + solver.getNumberFCEvals());
        System.out.println("Current feasError= " + solver.getAbsFeasError());

        return 0;
    }
}
