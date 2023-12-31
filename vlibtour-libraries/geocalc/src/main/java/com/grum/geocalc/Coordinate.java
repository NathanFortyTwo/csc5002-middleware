/*
 * BSD 3-Clause License
 *
 * Copyright (c) 2015, Grumlimited Ltd (Romain Gallet)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 *  Neither the name of Geocalc nor the names of its
 *   contributors may be used to endorse or promote products derived from
 *   this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.grum.geocalc;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.Math.*;

/**
 * Represent a coordinate decimalDegrees, in degrees
 *
 * @author rgallet
 */
public abstract class Coordinate implements Serializable {
    /**
	 * to serialize.
	 */
	private static final long serialVersionUID = 1L;
	
	//degrees
    double decimalDegrees;

    public double getValue() {
        return decimalDegrees;
    }

    public double getDecimalDegrees() {
        return decimalDegrees;
    }

    @Override
    public String toString() {
        return "DegreeCoordinate{" + "decimalDegrees=" + decimalDegrees + " degrees}";
    }

    DMSCoordinate getDMSCoordinate() {
        double wholeDegrees = (int) decimalDegrees;
        double remaining = abs(decimalDegrees - wholeDegrees);
        double minutes = (int) (remaining * 60);
        remaining = remaining * 60 - minutes;
        double seconds = BigDecimal.valueOf(remaining * 60).setScale(4, RoundingMode.HALF_UP).doubleValue();

        return new DMSCoordinate(wholeDegrees, minutes, seconds);
    }

    DegreeCoordinate getDegreeCoordinate() {
        return new DegreeCoordinate(decimalDegrees);
    }

    GPSCoordinate getGPSCoordinate() {
        double wholeDegrees = floor(decimalDegrees);
        double remaining = decimalDegrees - wholeDegrees;
        double minutes = floor(remaining * 60);

        return new GPSCoordinate(wholeDegrees, minutes);
    }

    RadianCoordinate getRadianCoordinate() {
        return new RadianCoordinate(toRadians(decimalDegrees));
    }
}
