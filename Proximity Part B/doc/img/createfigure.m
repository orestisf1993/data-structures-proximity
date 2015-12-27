function createfigure(mod, power)
% Create figure

if ~exist('mod','var'), mod = 1; end
if ~exist('power','var'), power = 1; end

h = figure('visible','off', 'Menubar','none', 'defaulttextinterpreter','latex');
xdata = 1:1:20;
ydata = 1:1:6;
[xdata, ydata] = meshgrid(xdata, ydata);
eval = @(score, nn) mod * ((20 - score) / 20) .* score ./ (nn.^(power));
zdata = eval(xdata, ydata);

% Create mesh
surf(xdata,ydata,zdata);

hold('on');
grid('on');
% Create ylabel
ylabel('{free neighbors}');

% Create xlabel
xlabel('{score}');

% Create title
if (mod ~=1 && power ~=1)
    t = '$%.2f \\frac{\\frac{20 - score}{20}\\cdot score}{(free neighbors)^{%.2f}}$';
    t = sprintf(t, mod, power);
elseif (mod ~=1)
    t = '$%.2f \\frac{\\frac{20 - score}{20}\\cdot score}{(free neighbors)}$';
    t = sprintf(t, mod);
elseif (power ~=1)
    t = '$\\frac{\\frac{20 - score}{20}\\cdot score}{(free neighbors)^{%.2f}}$';
    t = sprintf(t, power);
else
    t = '$\\frac{\\frac{20 - score}{20}\\cdot score}{free neighbors}$';
    t = sprintf(t);
end
title(t);

% Set limits
set(gca,'xlim',[xdata(1) xdata(end)])
set(gca,'ylim',[ydata(1) ydata(end)])
set(gca,'zlim',[0 10])

% legend(num2str((threadDict_2(:))),'Location','NorthWest');
% set(gca, 'Xtick',1:length(sizeDict),'XTickLabel', sizeDict);
% xlabel('Number of nodes');
% ylabel('times');
% title ('Speedup');
portrait_print(h, 'res.pdf');