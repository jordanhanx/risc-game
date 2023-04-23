package edu.duke.ece651.team7.server.model;

public interface OrderVisitor<T> {
    public T visit(MoveOrder order);
    public T visit(AttackOrder order);
    public T visit(ResearchOrder order);
    public T visit(UpgradeOrder order);
    public T visit(AllianceOrder order);
    public T visit(ManufactureOrder order);
}
