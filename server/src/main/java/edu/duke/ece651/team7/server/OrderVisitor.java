package edu.duke.ece651.team7.server;

public interface OrderVisitor<T> {
    public T visit(MoveOrder order);
    public T visit(AttackOrder order);
    public T visit(ResearchOrder order);
    public T visit(UpgradeOrder order);
}
