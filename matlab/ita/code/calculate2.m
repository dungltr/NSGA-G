%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% MATLAB Code for epidemic simulations with the SIDARTHE model in the work
% The parameter estimated by NSGA-G
% Modelling the COVID-19 epidemic and implementation of population-wide interventions in Kazakhstan
% the original SIDARTHE code is published by Giulia Giordano et. al, April 5, 2020
% 
%  
% Contact: trung-dung.le@irisa.fr
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function R0_test = calculate2(alfa,r1,beta,epsilon,r2,gamma,zeta,r3,delta,eta,r4,theta);
R0_test = (alfa*r2*r3*r4+epsilon*beta*r3*r4+gamma*zeta*r2*r4+delta*eta*epsilon*r3+delta*zeta*theta*r2)/(r1*r2*r3*r4);
end
