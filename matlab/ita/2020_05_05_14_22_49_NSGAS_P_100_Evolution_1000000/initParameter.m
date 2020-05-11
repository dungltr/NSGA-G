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
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
function [alfa, beta, gamma, delta, epsilon, theta, zeta, eta, mu, nu, tau, lambda, rho, kappa, xi, sigma] = initParameter()
alfa=0.45842115176255355;
beta=0.038780002134314255;
gamma=0.1303550121219824;
delta=0.7392723449558701;
epsilon=0.06425868685262753;
theta=0.6204578569151948;
zeta=0.13473582850229954;
eta=0.23277053636212044;
mu=0.0889957401715623;
nu=0.29150350403438985;
tau=0.039142032142112385;
lambda=0.11101264411066632;
rho=0.009691531493745303;
kappa=0.10442401669469446;
xi=0.005628849627131962;
sigma=0.0245026304719993;
end
