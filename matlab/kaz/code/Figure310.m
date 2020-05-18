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
Time = 62/step;
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 3

Figureout3 = D+R+T+E+H_diagnosticati;
error3 = 0;
for r = 8:54
    error3 = error3 + (Figureout3(r*100) - CasiTotali(r))*(Figureout3(r*100) - CasiTotali(r));
end
SQE3 = 1.0273e-07
error3
%%%%%%%%%%%%%
figure
plot(t,D+R+T+E+H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),CasiTotali)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Cumulative Diagnosed Cases: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['CasiTotali.pdf'])
    print -djpg CasiTotaliF3.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 4
Figureout4 = H_diagnosticati;
error4 = 0;
for r = 8:54
    error4 = error4 + (Figureout4(r*100) - Guariti(r))*(Figureout4(r*100) - Guariti(r));
end
%SQE4 = 5.5446e-08
error4
figure
plot(t,H_diagnosticati)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Guariti)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Recovered: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Guariti_diagnosticati.pdf'])
    print -djpg Guariti_diagnosticatiF4.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 5
Figureout5 = E;
error5 = 0;
for r = 8:54
    error5 = error5 + (Figureout5(r*100) - Deceduti(r))*(Figureout5(r*100) - Deceduti(r));
end
SQE5 = 4.9117e-07
error5
figure
plot(t,E)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Deceduti)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Deaths: Model vs. Data - NOTE: EXCLUDED FROM FITTING')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Morti.pdf'])
    print -djpg MortiF5.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 6
Figureout6 = D+R+T;
error6 = 0;
for r = 8:54
    error6 = error6 + (Figureout6(r*100) - Positivi(r))*(Figureout6(r*100) - Positivi(r));
end
SQE6 = 3.9855e-07
error6
figure
plot(t,D+R+T)
hold on
stem(t(1:1/step:size(CasiTotali,2)/step),Positivi)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected: Model vs. Data')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['Positivi_diagnosticati.pdf'])
    print -djpg Positivi_diagnosticatiF6.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 7

figure
plot(t,D)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare)
xlim([t(1) t(Time)])
%ylim([0 1e-3])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, No Symptoms: Model estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiAsintomatici_diagnosticati.pdf'])
    print -djpg InfettiAsintomatici_diagnosticatiF7.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 8

figure
plot(t,R)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Ricoverati_sintomi)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, Symptoms: Model estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_ricoverati.pdf'])
    %print -djpg InfettiSintomatici_diagnosticati_ricoveratiF8.jpg
end
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 9
figure
plot(t,D+R)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Isolamento_domiciliare+Ricoverati_sintomi)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, No or Mild Symptoms: Model estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiNonGravi_diagnosticati.pdf'])
    print -djpg InfettiNonGravi_diagnosticatiF9.jpg
end

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Figure 10
figure
plot(t,T)
hold on
%stem(t(1+3/step:1/step:1+(size(Ricoverati_sintomi,2)+2)/step),Terapia_intensiva)
xlim([t(1) t(Time)])
ylim([0 2.5e-3])
axis 'auto y'
title('Infected, Life-Threatening Symptoms: Model estimated by NSGA-G')
xlabel('Time (days)')
ylabel('Cases (fraction of the population)')
grid

if plotPDF==1
    set(gcf, 'PaperUnits', 'centimeters');
    set(gcf, 'PaperPosition', [0 0 16 10]);
    set(gcf, 'PaperSize', [16 10]); % dimension on x axis and y axis resp.
    print(gcf,'-dpdf', ['InfettiSintomatici_diagnosticati_terapiaintensiva.pdf'])
    print -djpg InfettiSintomatici_diagnosticati_terapiaintensivaF10.jpg
end

