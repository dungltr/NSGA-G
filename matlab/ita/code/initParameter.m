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
function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = initParameter()
alfa=0.3784350672463163;
beta=0.2393142107966432;
gamma=0.0045531267491586425;
delta=0.2224886524141444;
epsilon=0.08388150321808467;
theta=0.7292184743143447;
zeta=0.1021163405768431;
eta=0.29251479585338314;
mu=0.5487629463692846;
nu=0.0967749617700651;
tau=0.03214091076064725;
lambda=0.12869431096966;
rho=0.0012349377240047491;
kappa=0.1098560146190957;
xi=0.0014605819757646935;
sigma=0.05793770058176355;
end
