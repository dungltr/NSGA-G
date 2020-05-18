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
function [r1,r2,r3,r4,r5] = calParameter(epsilon,zeta,lambda,eta,rho,theta,mu,kappa,nu,xi,sigma,tau)
r1=epsilon+zeta+lambda;
r2=eta+rho;
r3=theta+mu+kappa;
r4=nu+xi;
r5=sigma+tau;
end
