/*
 * Copyright (C) 2010 Richard Lincoln
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */

package edu.cornell.pserc.jpower.tdouble.jpc;

import cern.colt.matrix.tdouble.DoubleFactory2D;
import cern.colt.matrix.tdouble.DoubleMatrix1D;
import cern.colt.matrix.tdouble.DoubleMatrix2D;
import cern.colt.matrix.tint.IntMatrix1D;
import edu.cornell.pserc.jpower.tdouble.util.Djp_util;

/**
 *
 * @author Richard Lincoln (r.w.lincoln@gmail.com)
 *
 */
public class Djp_gencost {

	private static final Djp_util util = new Djp_util();

	private static final int MODEL	= 0;
	private static final int STARTUP	= 1;
	private static final int SHUTDOWN	= 2;
	private static final int NCOST	= 3;
	private static final int COST		= 4;

	/** cost model, 1 = piecewise linear, 2 = polynomial */
	public IntMatrix1D model;

	/** startup cost in US dollars */
	public DoubleMatrix1D startup;

	/** shutdown cost in US dollars */
	public DoubleMatrix1D shutdown;

	/**
	 * number breakpoints in piecewise linear cost function,
	 * parameters defining total cost function begin in this col
	 */
	public IntMatrix1D ncost;

	/**
	 * (MODEL = 1) : p0, f0, p1, f1, ..., pn, fn
	 * where p0 < p1 < ... < pn and the cost f(p) is defined
	 * by the coordinates (p0,f0), (p1,f1), ..., (pn,fn) of
	 * the end/break-points of the piecewise linear cost
	 * (MODEL = 2) : cn, ..., c1, c0
	 * n+1 coefficients of an n-th order polynomial cost fcn,
	 * starting with highest order, where cost is
	 * f(p) = cn*p^2 + ... + c1*p + c0
	 */
	public DoubleMatrix2D cost;

	/**
	 *
	 * @return the number of generator costs.
	 */
	public int size() {
		return (int) this.model.size();
	}

	/**
	 *
	 * @return a copy of the gencost data.
	 */
	public Djp_gencost copy() {
		Djp_gencost other = new Djp_gencost();

		other.model = this.model.copy();
		other.startup = this.startup.copy();
		other.shutdown = this.shutdown.copy();
		other.ncost = this.ncost.copy();
		other.cost = this.cost.copy();

		return other;
	}

	/**
	 *
	 * @param other
	 */
	@SuppressWarnings("static-access")
	public void fromMatrix(DoubleMatrix2D other) {

		this.model = util.intm(other.viewColumn(MODEL));
		this.startup = other.viewColumn(STARTUP);
		this.shutdown = other.viewColumn(SHUTDOWN);
		this.ncost = util.intm(other.viewColumn(NCOST));
		this.cost = other.viewSelection(null, util.irange(COST, other.columns()));
	}

	/**
	 *
	 * @return
	 */
	@SuppressWarnings("static-access")
	public DoubleMatrix2D toMatrix() {
		DoubleMatrix2D matrix = DoubleFactory2D.dense.make(size(), util.max(ncost.toArray()) + 4);

		matrix.viewColumn(MODEL).assign( util.dblm(this.model) );
		matrix.viewColumn(MODEL).assign(this.startup);
		matrix.viewColumn(SHUTDOWN).assign(this.shutdown);
		matrix.viewColumn(NCOST).assign( util.dblm(this.ncost) );
		matrix.viewSelection(null, util.irange(COST, COST + this.cost.columns())).assign(this.cost);

		return matrix;
	}

}