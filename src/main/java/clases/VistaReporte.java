package clases;
// Clase que permite almacenar cada registro de la vista que se muestra en la interfaz
public class VistaReporte {
private String vicariaResponsable;
private int proyectosTotales;
private int enEjecucion;
private int planificados;
private int canceladosSuspendidos;
private String presupuestoTotal;
private String inversionPromedio;

    public VistaReporte(String vicariaResponsable, int proyectosTotales, int enEjecucion, int planificados, int canceladosSuspendidos, String presupuestoTotal, String inversionPromedio) {
        this.vicariaResponsable = vicariaResponsable;
        this.proyectosTotales = proyectosTotales;
        this.enEjecucion = enEjecucion;
        this.planificados = planificados;
        this.canceladosSuspendidos = canceladosSuspendidos;
        this.presupuestoTotal = presupuestoTotal;
        this.inversionPromedio = inversionPromedio;
    }

    public String getVicariaResponsable() {
        return vicariaResponsable;
    }

    public int getProyectosTotales() {
        return proyectosTotales;
    }

    public int getEnEjecucion() {
        return enEjecucion;
    }

    public int getPlanificados() {
        return planificados;
    }

    public int getCanceladosSuspendidos() {
        return canceladosSuspendidos;
    }

    public String getPresupuestoTotal() {
        return presupuestoTotal;
    }

    public String getInversionPromedio() {
        return inversionPromedio;
    }
}
